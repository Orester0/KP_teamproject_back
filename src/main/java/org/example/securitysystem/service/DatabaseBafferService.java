package org.example.securitysystem.service;

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
    private final LogService databaseService; // Припускаємо, що у вас є такий сервіс
    private static final int FLUSH_INTERVAL_SECONDS = 5;
    private static final int BUFFER_SIZE_THRESHOLD = 100;

    @Autowired
    public DatabaseBufferService(LogService databaseService) {
        this.databaseService = databaseService;
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

    public void addToBuffer(EventLogger.SensorLog log) {
        logBuffer.offer(log);

        // Якщо буфер досяг порогового значення, спробуємо його очистити
        if (logBuffer.size() >= BUFFER_SIZE_THRESHOLD) {
            flushBuffer();
        }
    }

    private void flushBuffer() {
        try {
            if (logBuffer.isEmpty()) {
                return;
            }


            int batchSize = 0;
List<EventLogger.SensorLog> logs = new ArrayList<>();
            // Збираємо всі доступні логи з буфера
            while ((log = logBuffer.poll()) != null) {
                logs.add(log);
                batchSize++;
            }

            if (batchSize > 0) {
                databaseService.createLog (logs);
                log.debug("Flushed {} log entries to database", batchSize);
            }
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

