package org.example.securitysystem.service;



import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
public class DatabaseBufferService {
    private final Queue<EventLogger.SensorLog> logBuffer = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final LogService logService;
    private static final int FLUSH_INTERVAL_SECONDS = 5;
    private static final int BUFFER_SIZE_THRESHOLD = 5;
    private final  long sessionId;

    @Autowired
    public DatabaseBufferService(LogService logService) {
        this.logService = logService;

        initializeBufferFlushing();
    }

    private void initializeBufferFlushing() {
        scheduler.scheduleAtFixedRate(
                this::flushBuffer,
                FLUSH_INTERVAL_SECONDS,
                FLUSH_INTERVAL_SECONDS,
                TimeUnit.SECONDS
        );
    }

    public void addToBuffer(EventLogger.SensorLog log,long sessionId) {
        logBuffer.offer(log);

        if (logBuffer.size() >= BUFFER_SIZE_THRESHOLD) {
            flushBuffer(sessionId);
        }
    }

    private void flushBuffer(long sessionId) {
        try {
            if (logBuffer.isEmpty()) {
                return;
            }

            logService.createLog(logBuffer,sessionId);


        } catch (Exception e) {
            log.error("Error flushing buffer to database: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            // Фінальна спроба зберегти всі логи перед вимкненням
            flushBuffer();

            scheduler.shutdown();
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

