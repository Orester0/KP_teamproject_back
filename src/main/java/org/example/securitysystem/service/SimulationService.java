package org.example.securitysystem.service;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.model_controller.Linker;
import org.example.securitysystem.model.model_controller.mediator.SecurityMediator;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;
import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
import org.example.securitysystem.model.model_controller.robber_simulator.RobberSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class SimulationService {
    private final WebSocketService webSocketService;
    private final Map<Long, SimulationContext> activeSimulations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private ExecutorService sensorTriggerExecutor = Executors.newFixedThreadPool(3); // 3 потоки для сенсорів
    private ExecutorService webSocketSenderExecutor = Executors.newSingleThreadExecutor(); // 1 потік для WebSocket
    private ExecutorService dbtSenderExecutor = Executors.newSingleThreadExecutor();
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 60;

    @Autowired
    public SimulationService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void startSimulation(Long sessionId, Building building, String socketTopic) {
        log.info("Starting simulation for session: {}", sessionId);

        stopSimulation(sessionId);

        SimulationContext context = createSimulationContext(building, socketTopic);
        activeSimulations.put(sessionId, context);

        // Запускаємо основний цикл симуляції
        ScheduledFuture<?> simulationTask = scheduler.scheduleAtFixedRate(
                () -> runMultiThreadedSimulationCycle(sessionId, context),
                0, 100, TimeUnit.MILLISECONDS
        );

        context.setSimulationTask(simulationTask);

        // Запускаємо потік для надсилання даних через WebSocket
        ScheduledFuture<?> webSocketTask = scheduler.scheduleAtFixedRate(
                () -> sendWebSocketUpdates(context),
                0, 1, TimeUnit.SECONDS
        );


        context.setWebSocketTask(webSocketTask);


        ScheduledFuture<?> dbTask = scheduler.scheduleAtFixedRate(
                () -> dbSocketUpdates(context),
                0, 10, TimeUnit.SECONDS
        );


        context.setWebSocketTask(dbTask);

        log.info("Simulation started successfully for session: {}", sessionId);
    }

    private SimulationContext createSimulationContext(Building building, String socketTopic) {
        SecurityMediator securityController = new SecurityMediator();
        SecurityEventManager securityEventManager = new SecurityEventManager();
        EventLogger eventLogger = new EventLogger();

        Linker linker = new Linker(building, securityController, securityEventManager, eventLogger);
        linker.link();

        RobberSimulator simulator = new RobberSimulator(building);

        return new SimulationContext(simulator,eventLogger, socketTopic);
    }

    private void runMultiThreadedSimulationCycle(Long sessionId, SimulationContext context) {
        if (context.isPaused()) {
            return;
        }

        List<Future<?>> sensorTasks = new ArrayList<>();
        try {
            // Запускаємо 3 паралельні задачі
            for (int i = 0; i < 3; i++) {
                Future<?> task = sensorTriggerExecutor.submit(() -> {
                    try {
                        context.getSimulator().triggerRandomSensor();
                    } catch (Exception e) {
                        log.error("Error in sensor trigger thread: {}", e.getMessage());
                    }
                });
                sensorTasks.add(task);
            }

            // Чекаємо завершення всіх задач з таймаутом
            for (Future<?> task : sensorTasks) {
                try {
                    task.get(8, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    log.warn("Sensor trigger task timed out for thread: {}", Thread.currentThread().getName());
                    task.cancel(true);
                } catch (Exception e) {
                    log.error("Error waiting for sensor trigger task: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Error in simulation cycle for session {}: {}", sessionId, e.getMessage(), e);
        }
    }

    private void sendWebSocketUpdates(SimulationContext context) {
        webSocketSenderExecutor.submit(() -> {
            try {
                String log = context.getEventLogger().getBuffer();
                if (!log.isEmpty()) {
                    String threadInfo = String.format("[WebSocketThread: %s] ", Thread.currentThread().getName());
                    log = threadInfo + log;
                    webSocketService.sendSimulationEvent(context.getSocketTopic(), log);
                    context.getEventLogger().clearBuffer();
                }
            } catch (Exception e) {
                log.error("Error in WebSocket sender thread: {}", e.getMessage());
            }
        });
    }

    private void dbSocketUpdates(SimulationContext context) {
        webSocketSenderExecutor.submit(() -> {
            try {
                String log = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                webSocketService.sendSimulationEvent(context.getSocketTopic(), log);
            } catch (Exception e) {
                log.error("Error in WebSocket sender thread: {}", e.getMessage());
            }
        });
    }

    public void pauseSimulation(Long sessionId) {
        if (sessionId == null) {
            log.warn("Cannot pause simulation: session ID is null");
            return;
        }

        SimulationContext context = activeSimulations.get(sessionId);
        if (context == null) {
            log.warn("Cannot pause simulation: no active simulation found for session {}", sessionId);
            return;
        }

        synchronized (context) {
            if (context.isPaused()) {
                log.info("Simulation for session {} is already paused", sessionId);
                return;
            }

            try {
                context.setPaused(true);
                webSocketService.sendSimulationEvent(context.getSocketTopic(), "Simulation paused");
                log.info("Simulation paused successfully for session: {}", sessionId);
            } catch (Exception e) {
                log.error("Error while pausing simulation for session {}: {}", sessionId, e.getMessage(), e);
            }
        }
    }

    public void resumeSimulation(Long sessionId) {
        if (sessionId == null) {
            log.warn("Cannot resume simulation: session ID is null");
            return;
        }

        SimulationContext context = activeSimulations.get(sessionId);
        if (context == null) {
            log.warn("Cannot resume simulation: no active simulation found for session {}", sessionId);
            return;
        }

        synchronized (context) {
            if (!context.isPaused()) {
                log.info("Simulation for session {} is already running", sessionId);
                return;
            }

            try {
                context.setPaused(false);
                webSocketService.sendSimulationEvent(context.getSocketTopic(), "Simulation resumed");
                log.info("Simulation resumed successfully for session: {}", sessionId);
            } catch (Exception e) {
                log.error("Error while resuming simulation for session {}: {}", sessionId, e.getMessage(), e);
            }
        }
    }


    public boolean isSimulationPaused(Long sessionId) {
        SimulationContext context = activeSimulations.get(sessionId);
        return context != null && context.isPaused();
    }

    public void stopSimulation(Long sessionId) {
        SimulationContext context = activeSimulations.remove(sessionId);
        if (context != null) {
            try {
                // Зупиняємо основну задачу симуляції
                ScheduledFuture<?> task = context.getSimulationTask();
                if (task != null && !task.isCancelled()) {
                    task.cancel(true);
                }

                // Зупиняємо задачу WebSocket
                ScheduledFuture<?> webSocketTask = context.getWebSocketTask();
                if (webSocketTask != null && !webSocketTask.isCancelled()) {
                    webSocketTask.cancel(true);
                }

                // Чекаємо завершення потоків
                sensorTriggerExecutor.shutdown();
                webSocketSenderExecutor.shutdown();

                if (!sensorTriggerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    sensorTriggerExecutor.shutdownNow();
                }
                if (!webSocketSenderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    webSocketSenderExecutor.shutdownNow();
                }

                // Створюємо нові екземпляри ExecutorService для майбутніх симуляцій
                sensorTriggerExecutor = Executors.newFixedThreadPool(3);
                webSocketSenderExecutor = Executors.newSingleThreadExecutor();

                log.info("Simulation stopped for session: {}", sessionId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while stopping simulation for session: {}", sessionId);
            }
        }
    }

    @PreDestroy
    public void cleanup() {
        log.info("Starting SimulationService cleanup...");

        // Зупиняємо всі активні симуляції
        activeSimulations.keySet().forEach(this::stopSimulation);

        // Зупиняємо scheduler
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
                if (!scheduler.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    log.error("Scheduler did not terminate");
                }
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Зупиняємо executor сенсорів
        sensorTriggerExecutor.shutdown();
        try {
            if (!sensorTriggerExecutor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                sensorTriggerExecutor.shutdownNow();
                if (!sensorTriggerExecutor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    log.error("Sensor trigger executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            sensorTriggerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Зупиняємо executor для WebSocket
        webSocketSenderExecutor.shutdown();
        try {
            if (!webSocketSenderExecutor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                webSocketSenderExecutor.shutdownNow();
                if (!webSocketSenderExecutor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    log.error("WebSocket sender executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            webSocketSenderExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        log.info("SimulationService cleanup completed");
    }

    private static class SimulationContext {
        @Getter
        private final RobberSimulator simulator;
        @Getter
        private final EventLogger eventLogger;

        @Getter
        private final String socketTopic;
        @Setter
        @Getter
        private ScheduledFuture<?> simulationTask;
        @Setter
        @Getter
        private ScheduledFuture<?> webSocketTask;
        @Setter
        @Getter
        private volatile boolean paused;

        public SimulationContext(RobberSimulator simulator,
                                 EventLogger eventLogger, String socketTopic) {
            this.simulator = simulator;
            this.eventLogger = eventLogger;
            this.socketTopic = socketTopic;
            this.paused = false;
        }
    }
}
