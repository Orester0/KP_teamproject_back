package org.example.securitysystem.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.model.dto.SensorLog;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.model_controller.Linker;
import org.example.securitysystem.model.model_controller.mediator.SecurityMediator;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;
import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
import org.example.securitysystem.model.model_controller.robber_simulator.RobberSimulator;
import org.example.securitysystem.model.models_db.EventLog;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SimulationReplayService {
    private final WebSocketService webSocketService;
    private final LogService logService;
    private final Map<Long, ReplayContext> activeReplays = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");

    public SimulationReplayService(WebSocketService webSocketService, LogService logService) {
        this.webSocketService = webSocketService;
        this.logService = logService;

    }

    public void startReplay(Long sessionId, Building building, String socketTopic,
                             LocalDateTime startTime, LocalDateTime endTime) {
        stopReplay(sessionId);

        List<SensorLog> filteredLogs = filterLogsByTimeRange(sessionId, startTime, endTime);
        if (filteredLogs.isEmpty()) {
            log.warn("No logs found for the specified time range");
            return;
        }

        ReplayContext context = createReplayContext(building, socketTopic, filteredLogs);
        activeReplays.put(sessionId, context);

        scheduleReplayCycle(sessionId, context, 0);

        log.info("Replay started for session: {} from {} to {}",
                sessionId, startTime.format(FORMATTER), endTime.format(FORMATTER));
    }

    private List<SensorLog> filterLogsByTimeRange(long sessionId,
                                                              LocalDateTime startTime,
                                                              LocalDateTime endTime) {
        List<SensorLog> logs = logService.getEventLogs(sessionId,null,null,null);
        return logs.stream()
                .filter(log -> {
                    LocalDateTime logTime = LocalDateTime.parse(log.currentTime().toString(), FORMATTER);
                    return !logTime.isBefore(startTime) && !logTime.isAfter(endTime);
                })
                .sorted(Comparator.comparing(log ->
                        LocalDateTime.parse(log.currentTime().toString(), FORMATTER)))
                .collect(Collectors.toList());
    }

    private ReplayContext createReplayContext(Building building,
                                              String socketTopic,
                                              List<SensorLog> filteredLogs) {
        SecurityMediator securityController = new SecurityMediator();
        SecurityEventManager securityEventManager = new SecurityEventManager();
        EventLogger eventLogger = new EventLogger();


        Linker linker = new Linker(building, securityController, securityEventManager, eventLogger);
        linker.link();

        return new ReplayContext(building, eventLogger, linker, socketTopic, filteredLogs);
    }

    private void scheduleReplayCycle(Long sessionId, ReplayContext context, long delay) {
        context.setReplayTask(scheduler.schedule(() -> {
            try {
                if (!context.isPaused() && context.getCurrentLogIndex() < context.getFilteredLogs().size()) {
                    SensorLog currentLog = context.getFilteredLogs().get(context.getCurrentLogIndex());

                    // Виконання основного циклу
                    Sensor sensorFromLog = (Sensor) currentLog.sensorDetails();
                    boolean activated = currentLog.activated();

                    Sensor buildingSensor = context.getBuilding().getFloors().stream()
                            .flatMap(floor -> floor.getSensors().stream())
                            .filter(s -> s.getID() == sensorFromLog.getID())
                            .findFirst()
                            .orElseThrow();

                    buildingSensor.detect();

                    // Надсилання логів через WebSocket
                    String log = context.getEventLogger().getBuffer();
                    if (!log.isEmpty()) {
                        webSocketService.sendSimulationEvent(context.getSocketTopic(), log);
                        context.getEventLogger().clearBuffer();
                    }

                    // Обчислення затримки для наступного виконання
                    int nextIndex = context.getCurrentLogIndex() + 1;
                    long nextDelay = 0;
                    if (nextIndex < context.getFilteredLogs().size()) {
                        LocalDateTime currentTime = LocalDateTime.parse(currentLog.currentTime().toString(), FORMATTER);
                        LocalDateTime nextTime = LocalDateTime.parse(
                                context.getFilteredLogs().get(nextIndex).currentTime().toString(), FORMATTER);

                        nextDelay = Duration.between(currentTime, nextTime).toMillis();
                    }

                    context.incrementLogIndex();

                    // Перепланування наступного виконання
                    if (context.getCurrentLogIndex() < context.getFilteredLogs().size()) {
                        scheduleReplayCycle(sessionId, context, nextDelay);
                    } else {
                        stopReplay(sessionId);
                    }
                }
            } catch (Exception e) {
                log.error("Error in replay cycle for session {}: {}", sessionId, e.getMessage(), e);
            }
        }, delay, TimeUnit.MILLISECONDS));
    }


    public void pauseReplay(Long sessionId) {
        ReplayContext context = activeReplays.get(sessionId);
        if (context != null) {
            context.setPaused(true);
            log.info("Replay paused for session: {}", sessionId);
        }
    }

    public void resumeReplay(Long sessionId) {
        ReplayContext context = activeReplays.get(sessionId);
        if (context != null) {
            context.setPaused(false);
            log.info("Replay resumed for session: {}", sessionId);
        }
    }

    public void stopReplay(Long sessionId) {
        ReplayContext context = activeReplays.remove(sessionId);
        if (context != null) {
            ScheduledFuture<?> task = context.getReplayTask();
            if (task != null && !task.isCancelled()) {
                task.cancel(true);
            }
            log.info("Replay stopped for session: {}", sessionId);
        }
    }

    @Getter
    private static class ReplayContext {
        private final Building building;
        private final EventLogger eventLogger;
        private final Linker linker;
        private final String socketTopic;
        private final List<SensorLog> filteredLogs;
        private int currentLogIndex;
        private volatile boolean paused;
        private ScheduledFuture<?> replayTask;


        public ReplayContext(  Building building, EventLogger eventLogger,
                             Linker linker, String socketTopic,
                             List<SensorLog> filteredLogs) {
            this.building = building;
            this.eventLogger = eventLogger;
            this.linker = linker;
            this.socketTopic = socketTopic;
            this.filteredLogs = filteredLogs;
            this.currentLogIndex = 0;
            this.paused = false;

        }

        public void incrementLogIndex() {
            currentLogIndex++;
        }

        public void setPaused(boolean paused) {
            this.paused = paused;
        }

        public void setReplayTask(ScheduledFuture<?> replayTask) {
            this.replayTask = replayTask;
        }
    }
}