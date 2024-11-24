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

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class SimulationService {
    private final WebSocketService webSocketService;

    private final Map<Long, SimulationContext> activeSimulations = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);

    @Autowired
    public SimulationService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void startSimulation(Long sessionId, Building building, String socketTopic) {
        log.info("Starting simulation for session: {}", sessionId);

        // Stop any existing simulation for this session
        stopSimulation(sessionId);

        // Create new simulation context
        SimulationContext context = createSimulationContext(building, socketTopic);
        activeSimulations.put(sessionId, context);

        // Schedule simulation updates
        ScheduledFuture<?> simulationTask = scheduler.scheduleAtFixedRate(
                () -> runSimulationCycle(sessionId, context),
                0, 1, TimeUnit.SECONDS
        );

        context.setSimulationTask(simulationTask);
        log.info("Simulation started successfully for session: {}", sessionId);
    }

    private SimulationContext createSimulationContext(Building building, String socketTopic) {
        // Create EventLogger

        SecurityMediator securityController = new SecurityMediator();
        SecurityEventManager securityEventManager = new SecurityEventManager();
        EventLogger eventLogger = new EventLogger();

        Linker linker = new Linker(building, securityController, securityEventManager, eventLogger);
        linker.link();

        // Create Linker and initialize security system components

        // Create RobberSimulator with the linked building
        RobberSimulator simulator = new RobberSimulator(building);

        return new SimulationContext(building, simulator, eventLogger, linker, socketTopic);
    }

    private void runSimulationCycle(Long sessionId, SimulationContext context) {
        try {
            if (!context.isPaused()) {
                context.getSimulator().triggerRandomSensor();
                String log = context.getEventLogger().getBuffer();

                //databaseService.writeBuffer();
                if (!log.isEmpty()) {
                    webSocketService.sendSimulationEvent(context.getSocketTopic(), log);
                    context.getEventLogger().clearBuffer();
                }
            }
        } catch (Exception e) {
            log.error("Error in simulation cycle for session {}: {}", sessionId, e.getMessage(), e);
        }
    }

    public void pauseSimulation(Long sessionId) {
        SimulationContext context = activeSimulations.get(sessionId);
        if (context != null) {
            context.setPaused(true);
            log.info("Simulation paused for session: {}", sessionId);
        }
    }

    public void resumeSimulation(Long sessionId) {
        SimulationContext context = activeSimulations.get(sessionId);
        if (context != null) {
            context.setPaused(false);
            log.info("Simulation resumed for session: {}", sessionId);
        }
    }

    public boolean isSimulationPaused(Long sessionId) {
        SimulationContext context = activeSimulations.get(sessionId);
        return context != null && context.isPaused();
    }

    public void stopSimulation(Long sessionId) {
        SimulationContext context = activeSimulations.remove(sessionId);
        if (context != null) {
            ScheduledFuture<?> task = context.getSimulationTask();
            if (task != null && !task.isCancelled()) {
                task.cancel(true);
            }
            context.getSimulator().stopSimulation();
            log.info("Simulation stopped for session: {}", sessionId);
        }
    }

    // Helper class to maintain simulation state and components
    private static class SimulationContext {
        private final Building building;
        // Getters and setters
        @Getter
        private final RobberSimulator simulator;
        @Getter
        private final EventLogger eventLogger;
        private final Linker linker;
        @Getter
        private final String socketTopic;
        @Setter
        @Getter
        private ScheduledFuture<?> simulationTask;
        @Setter
        @Getter
        private volatile boolean paused;

        public SimulationContext(Building building, RobberSimulator simulator,
                                 EventLogger eventLogger, Linker linker, String socketTopic) {
            this.building = building;
            this.simulator = simulator;
            this.eventLogger = eventLogger;
            this.linker = linker;
            this.socketTopic = socketTopic;
            this.paused = false;
        }

    }

    // Clean up method to be called when application shuts down
    @PreDestroy
    public void cleanup() {
        activeSimulations.keySet().forEach(this::stopSimulation);
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}