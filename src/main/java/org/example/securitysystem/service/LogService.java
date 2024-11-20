package org.example.securitysystem.service;

import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.model.models_db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class LogService {

    @Autowired
    private ILogRepository logRepository; // Репозиторій для збереження логів

    @Autowired
    private ISensorRepository sensorRepository;

    public LogService(ILogRepository logRepository,ISensorRepository sensorRepository){
        this.logRepository = logRepository;
        this.sensorRepository = sensorRepository;
    }

    public EventLog createEventLog(long sensorId) {

        SensorDB sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        RoomDB room = sensor.getRoom(); // Через сенсор отримуємо кімнату
        FloorDB floor = room.getFloor(); // Через кімнату отримуємо поверх
        SessionDB session = floor.getSession(); // Через поверх отримуємо сесію

        EventLog eventLog = new EventLog();
        eventLog.setSensor(sensor); // Встановлюємо сенсор
        eventLog.setRoom(room); // Встановлюємо кімнату
        eventLog.setFloor(floor); // Встановлюємо поверх
        eventLog.setSession(session); // Встановлюємо сесію
        eventLog.setStartTime(LocalDateTime.now()); // Встановлюємо час початку

        return logRepository.save(eventLog);
    }

    public List<EventLog> getEventLogsBySensorId(long sensorId){
        return logRepository.findBySensor_SensorId(sensorId);
    }

    public List<EventLog> getEventLogs(Long sessionId, Long floorId, Long roomId, String sensorType) {

        Specification<EventLog> specification = EventLogSpecification.withFilters(sessionId, floorId, roomId, sensorType);

        return logRepository.findAll(specification);
    }
}
