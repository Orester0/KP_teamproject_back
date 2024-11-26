package org.example.securitysystem.service.application_service.implementations;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.domain_service.Linker;
import org.example.securitysystem.service.domain_service.mediator.SecurityMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.example.securitysystem.service.domain_service.observer.listener.EventLogger;
import org.example.securitysystem.service.domain_service.robber_simulator.RobberSimulator;
import org.example.securitysystem.service.application_service.interfaces.ISimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class SimulationService implements ISimulationService {
    private final WebSocketService webSocketService;
    private final Map<Long, SimulationContext> activeSimulations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    private ExecutorService sensorTriggerExecutor;// 3 потоки для сенсорів
    private ExecutorService webSocketSenderExecutor = Executors.newSingleThreadExecutor(); // 1 потік для WebSocket
    private ExecutorService dbSenderExecutor = Executors.newSingleThreadExecutor();
    private final LogService logService;
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 60;

    @Autowired
    public SimulationService(WebSocketService webSocketService, LogService logService) {
        this.webSocketService = webSocketService;
        this.logService = logService;
    }

    public void start(Long sessionId, Building building, String socketTopic) {
        log.info("Starting simulation for session: {}", sessionId);

        stop(sessionId);

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
                () -> dbSocketUpdates(context,sessionId),
                0, 10, TimeUnit.SECONDS
        );

        context.setDbTask(dbTask);

        log.info("Simulation started successfully for session: {}", sessionId);
    }

    private SimulationContext createSimulationContext(Building building,
                                                      String socketTopic) {
        SecurityMediator securityController = new SecurityMediator();
        SecurityEventManager securityEventManager = new SecurityEventManager();
        EventLogger eventLogger = new EventLogger();

        Linker linker = new Linker(building, securityController, securityEventManager, eventLogger);
        linker.link();

        RobberSimulator simulator = new RobberSimulator(building);
         sensorTriggerExecutor = Executors.newFixedThreadPool(building.getFloors().size());

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
                List<EventLogger.SensorLog> objectList = context.getEventLogger().getObjectList();
                StringBuilder jsonPayload = new StringBuilder();
                for (var obj : objectList) {

                    if (obj.sensorDetails() instanceof Sensor) {
                        jsonPayload.append(String.format(
                                "{ \"sensorId\": \"%s\", \"time\": \"%s\" } \n",
                                ((Sensor) obj.sensorDetails()).getID(),
                                obj.currentTime()
                        ));
                    } else {
                        jsonPayload.append(String.format(
                                "{ \"name\": \"%s\", \"activated\": \"%s\", \"time\": \"%s\" } \n",
                                ((AlarmSystem) obj.sensorDetails()).getClass().getSimpleName(),
                                obj.activated(),
                                obj.currentTime()
                        ));
                    }
                }
                if (!jsonPayload.isEmpty()) {
                    webSocketService.sendSimulationEvent(context.getSocketTopic(), jsonPayload.toString());
                }
                context.getEventLogger().clearObjectList();

            } catch (Exception e) {
                log.error("Error in WebSocket sender thread: {}", e.getMessage());
            }
        });
    }

    private void dbSocketUpdates(SimulationContext context, long sessionId) {
        dbSenderExecutor.submit(() -> {
            try {
                List< EventLogger.SensorLog> logs = context.getEventLogger().getObjectList2();
                logService.createLog(logs,sessionId);
                context.getEventLogger().clearObjectList2();
            } catch (Exception e) {
                log.error("Error in WebSocket sender thread: {}", e.getMessage());
            }
        });
    }

    public void pause(Long sessionId) {
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

    public void resume(Long sessionId) {
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

    public void stop(Long sessionId) {
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
                ScheduledFuture<?> dbTask = context.getDbTask();
                if (dbTask != null && !dbTask.isCancelled()) {
                    dbTask.cancel(true);
                }

                // Чекаємо завершення потоків
                sensorTriggerExecutor.shutdown();
                webSocketSenderExecutor.shutdown();
                dbSenderExecutor.shutdown();

                if (!sensorTriggerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    sensorTriggerExecutor.shutdownNow();
                }
                if (!webSocketSenderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    webSocketSenderExecutor.shutdownNow();
                }
                if (!dbSenderExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    dbSenderExecutor.shutdownNow();
                }

                // Створюємо нові екземпляри ExecutorService для майбутніх симуляцій
                sensorTriggerExecutor = Executors.newFixedThreadPool(context.getSimulator().getBuilding().getFloors().size());
                webSocketSenderExecutor = Executors.newSingleThreadExecutor();
                dbSenderExecutor = Executors.newSingleThreadExecutor();

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
        activeSimulations.keySet().forEach(this::stop);

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

        // Зупиняємо executor для DB
        dbSenderExecutor.shutdown();
        try {
            if (!dbSenderExecutor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                dbSenderExecutor.shutdownNow();
                if (!dbSenderExecutor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    log.error("DB sender executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            dbSenderExecutor.shutdownNow();
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
        private ScheduledFuture<?> dbTask;
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