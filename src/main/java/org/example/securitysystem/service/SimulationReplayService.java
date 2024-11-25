//package org.example.securitysystem.service;
//
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import org.example.securitysystem.model.entity.building.Building;
//import org.example.securitysystem.model.model_controller.Linker;
//import org.example.securitysystem.model.model_controller.mediator.SecurityMediator;
//import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;
//import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.concurrent.*;
//
//@Slf4j
//@Service
//public class SimulationReplayService {
//    private final WebSocketService webSocketService;
//    private final Map<Long, ReplayContext> activeReplays = new ConcurrentHashMap<>();
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
//    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
//
//    public SimulationReplayService(WebSocketService webSocketService) {
//        this.webSocketService = webSocketService;
//    }
//
//    public void startReplay(Long sessionId, Building building, String socketTopic,
//                            List<SensorLog> logs, LocalDateTime startTime, LocalDateTime endTime) {
//        stopReplay(sessionId);
//
//        List<SensorLog> filteredLogs = filterLogsByTimeRange(logs, startTime, endTime);
//        if (filteredLogs.isEmpty()) {
//            log.warn("No logs found for the specified time range");
//            return;
//        }
//
//        ReplayContext context = createReplayContext(building, socketTopic, filteredLogs);
//        activeReplays.put(sessionId, context);
//
//        ScheduledFuture<?> replayTask = scheduler.scheduleAtFixedRate(
//                () -> runReplayCycle(sessionId, context),
//                0, 1, TimeUnit.SECONDS
//        );
//
//        context.setReplayTask(replayTask);
//        log.info("Replay started for session: {} from {} to {}",
//                sessionId, startTime.format(FORMATTER), endTime.format(FORMATTER));
//    }
//
//    private List<SensorLog> filterLogsByTimeRange(List<SensorLog> logs,
//                                                  LocalDateTime startTime,
//                                                  LocalDateTime endTime) {
//        return logs.stream()
//                .filter(log -> {
//                    LocalDateTime logTime = LocalDateTime.parse(log.getTimestamp(), FORMATTER);
//                    return !logTime.isBefore(startTime) && !logTime.isAfter(endTime);
//                })
//                .sorted(Comparator.comparing(log ->
//                        LocalDateTime.parse(log.getTimestamp(), FORMATTER)))
//                .collect(Collectors.toList());
//    }
//
//    private ReplayContext createReplayContext(Building building,
//                                              String socketTopic,
//                                              List<SensorLog> filteredLogs) {
//        SecurityMediator securityController = new SecurityMediator();
//        SecurityEventManager securityEventManager = new SecurityEventManager();
//        EventLogger eventLogger = new EventLogger();
//
//        Linker linker = new Linker(building, securityController, securityEventManager, eventLogger);
//        linker.link();
//
//        return new ReplayContext(building, eventLogger, linker, socketTopic, filteredLogs);
//    }
//
//    private void runReplayCycle(Long sessionId, ReplayContext context) {
//        try {
//            if (!context.isPaused() && context.getCurrentLogIndex() < context.getFilteredLogs().size()) {
//                SensorLog currentLog = context.getFilteredLogs().get(context.getCurrentLogIndex());
//
//                // Trigger the sensor based on the log
//                String sensorType = currentLog.getSensorDetails().getClass().getSimpleName();
//                boolean activated = currentLog.isActivated();
//
//                // Simulate the sensor trigger
//                context.getLinker().triggerSensorByType(sensorType, activated);
//
//                // Get and send the event log
//                String log = context.getEventLogger().getBuffer();
//                if (!log.isEmpty()) {
//                    webSocketService.sendSimulationEvent(context.getSocketTopic(), log);
//                    context.getEventLogger().clearBuffer();
//                }
//
//                context.incrementLogIndex();
//
//                // Check if replay is complete
//                if (context.getCurrentLogIndex() >= context.getFilteredLogs().size()) {
//                    log.info("Replay completed for session: {}", sessionId);
//                    stopReplay(sessionId);
//                }
//            }
//        } catch (Exception e) {
//            log.error("Error in replay cycle for session {}: {}", sessionId, e.getMessage(), e);
//        }
//    }
//
//    public void pauseReplay(Long sessionId) {
//        ReplayContext context = activeReplays.get(sessionId);
//        if (context != null) {
//            context.setPaused(true);
//            log.info("Replay paused for session: {}", sessionId);
//        }
//    }
//
//    public void resumeReplay(Long sessionId) {
//        ReplayContext context = activeReplays.get(sessionId);
//        if (context != null) {
//            context.setPaused(false);
//            log.info("Replay resumed for session: {}", sessionId);
//        }
//    }
//
//    public void stopReplay(Long sessionId) {
//        ReplayContext context = activeReplays.remove(sessionId);
//        if (context != null) {
//            ScheduledFuture<?> task = context.getReplayTask();
//            if (task != null && !task.isCancelled()) {
//                task.cancel(true);
//            }
//            log.info("Replay stopped for session: {}", sessionId);
//        }
//    }
//
//    @Getter
//    private static class ReplayContext {
//        private final Building building;
//        private final EventLogger eventLogger;
//        private final Linker linker;
//        private final String socketTopic;
//        private final List<SensorLog> filteredLogs;
//        private int currentLogIndex;
//        private volatile boolean paused;
//        private ScheduledFuture<?> replayTask;
//
//        public ReplayContext(Building building, EventLogger eventLogger,
//                             Linker linker, String socketTopic,
//                             List<SensorLog> filteredLogs) {
//            this.building = building;
//            this.eventLogger = eventLogger;
//            this.linker = linker;
//            this.socketTopic = socketTopic;
//            this.filteredLogs = filteredLogs;
//            this.currentLogIndex = 0;
//            this.paused = false;
//        }
//
//        public void incrementLogIndex() {
//            currentLogIndex++;
//        }
//
//        public void setPaused(boolean paused) {
//            this.paused = paused;
//        }
//
//        public void setReplayTask(ScheduledFuture<?> replayTask) {
//            this.replayTask = replayTask;
//        }
//    }
//}