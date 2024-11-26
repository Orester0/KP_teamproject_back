package org.example.securitysystem.service.application_service.implementations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.model.dto.SensorLog;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.domain_service.Linker;
import org.example.securitysystem.service.domain_service.mediator.SecurityMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.example.securitysystem.service.domain_service.observer.listener.EventLogger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReplayService {
    private final WebSocketService webSocketService;
    private final LogService logService;
    private final Map<Long, ReplayContext> activeReplays = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public ReplayService(WebSocketService webSocketService, LogService logService) {
        this.webSocketService = webSocketService;
        this.logService = logService;
    }

    public void startReplay(Long sessionId, Building building, String socketTopic,
                            LocalDateTime startTime, LocalDateTime endTime) {
        try {
            stopReplay(sessionId);

            List<SensorLog> filteredLogs = filterLogsByTimeRange(sessionId, startTime, endTime);
            if (filteredLogs.isEmpty()) {
                log.warn("No logs found for the specified time range");
                webSocketService.sendSimulationEvent(socketTopic, "No logs found for replay");
                return;
            }

            ReplayContext context = createReplayContext(building, socketTopic, filteredLogs);
            activeReplays.put(sessionId, context);



            scheduleReplayCycle(sessionId, context, 0);
            log.info("Replay started for session: {} from {} to {}",
                    sessionId, startTime.format(OUTPUT_FORMATTER), endTime.format(OUTPUT_FORMATTER));
        } catch (Exception e) {
            log.error("Unexpected error in startReplay: {}", e.getMessage(), e);
            throw e;
        }
    }

    private List<SensorLog> filterLogsByTimeRange(long sessionId,
                                                  LocalDateTime startTime,
                                                  LocalDateTime endTime) {
        try {
            List<SensorLog> logs = logService.getEventLogs(sessionId, null, null, null);
            return logs.stream()
                    .filter(log -> {
                        LocalDateTime logTime = LocalDateTime.parse(log.getCurrentTime(), OUTPUT_FORMATTER);
                        return !logTime.isBefore(startTime) && !logTime.isAfter(endTime);
                    })
                    .sorted(Comparator.comparing(log -> LocalDateTime.parse(log.getCurrentTime(), OUTPUT_FORMATTER)))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error filtering logs: {}", e.getMessage());
            return Collections.emptyList();
        }
    }


    private ReplayContext createReplayContext(Building building,
                                              String socketTopic,
                                              List<SensorLog> filteredLogs) {

        SecurityMediator securityController = new SecurityMediator();
        SecurityEventManager securityEventManager = new SecurityEventManager();
        EventLogger eventLogger = new EventLogger();

        Linker linker = new Linker(building, securityController, securityEventManager, eventLogger);
        linker.link();

        return new ReplayContext(building, eventLogger, socketTopic, filteredLogs);
    }

    private void scheduleReplayCycle(Long sessionId, ReplayContext context, long delay) {
        context.setReplayTask(scheduler.schedule(() -> {
            try {
                if (!context.isPaused() && context.getCurrentLogIndex() < context.getFilteredLogs().size()) {
                    SensorLog currentLog = context.getFilteredLogs().get(context.getCurrentLogIndex());
                    processSecurityColleague(context.getBuilding(), currentLog);

                    List<EventLogger.SensorLog> list = context.getEventLogger().getObjectList();
                    StringBuilder jsonPayload = new StringBuilder();

                    for (var obj : list) {

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

                    long nextDelay = calculateNextDelay(context);
                    context.incrementLogIndex();

                    if (context.getCurrentLogIndex() < context.getFilteredLogs().size()) {
                        scheduleReplayCycle(sessionId, context, nextDelay);
                    } else {
                        stopReplay(sessionId);
                    }
                }
            } catch (Exception e) {
                log.error("Error in replay cycle for session {}: {}", sessionId, e.getMessage(), e);
                stopReplay(sessionId);
            }
        }, delay, TimeUnit.MILLISECONDS));
    }



    private void processSecurityColleague(Building building, SensorLog log) {
        ISecurityColleague securityColleague = log.getSensorDetails();

        if (securityColleague instanceof Sensor) {
            Sensor sensor = (Sensor) securityColleague;

            building.getFloors().stream()
                    .flatMap(floor -> floor.getSensors().stream())
                    .filter(s -> s.getID() == sensor.getID())
                    .findFirst()
                    .ifPresent(s -> {
                        try {
                            s.detect();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private long calculateNextDelay(ReplayContext context) {
        int nextIndex = context.getCurrentLogIndex() + 1;
        if (nextIndex < context.getFilteredLogs().size()) {
            LocalDateTime currentTime = LocalDateTime.parse(
                    context.getFilteredLogs().get(context.getCurrentLogIndex()).getCurrentTime(),
                    OUTPUT_FORMATTER);
            LocalDateTime nextTime = LocalDateTime.parse(
                    context.getFilteredLogs().get(nextIndex).getCurrentTime(),
                    OUTPUT_FORMATTER);

            long delay = Math.max(0, java.time.Duration.between(currentTime, nextTime).toMillis());
            return Math.min(delay, 5000); // Обмежуємо затримку до 5 секунд
        }
        return 0;
    }


    public void stopReplay(Long sessionId) {
        ReplayContext context = activeReplays.remove(sessionId);
        if (context != null) {
            ScheduledFuture<?> task = context.getReplayTask();
            if (task != null && !task.isCancelled()) {
                task.cancel(true);
            }
            webSocketService.sendSimulationEvent(context.getSocketTopic(), "Replay stopped");
            log.info("Replay stopped for session: {}", sessionId);
        }
    }

    @Data
    private static class ReplayContext {
        private final Building building;
        private final EventLogger eventLogger;
        private final String socketTopic;
        private final List<SensorLog> filteredLogs;
        private int currentLogIndex;
        private volatile boolean paused;
        private ScheduledFuture<?> replayTask;

        public ReplayContext(Building building,
                             EventLogger eventLogger,
                             String socketTopic,
                             List<SensorLog> filteredLogs) {
            this.building = building;
            this.eventLogger = eventLogger;
            this.socketTopic = socketTopic;
            this.filteredLogs = filteredLogs;
            this.currentLogIndex = 0;
            this.paused = false;
        }

        public void incrementLogIndex() {
            currentLogIndex++;
        }

    }
}