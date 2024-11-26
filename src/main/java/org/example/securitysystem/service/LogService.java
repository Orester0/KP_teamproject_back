package org.example.securitysystem.service;

import lombok.extern.slf4j.Slf4j;
import org.example.securitysystem.mappers.EventLogMapper;
import org.example.securitysystem.model.dto.SensorLog;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
import org.example.securitysystem.model.models_db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


@Service
public class LogService {

    @Autowired
    private ILogRepository logRepository;

    @Autowired
    private ISensorRepository sensorRepository;

    @Autowired
    private IAlarmRepository alarmRepository;

    @Autowired
    private ISessionRepository sessionRepository;

    public LogService(ILogRepository logRepository,ISensorRepository sensorRepository,IAlarmRepository alarmRepository,ISessionRepository sessionRepository){
        this.logRepository = logRepository;
        this.sensorRepository = sensorRepository;
        this.alarmRepository = alarmRepository;
        this.sessionRepository = sessionRepository;
    }

    public EventLog createEventLog(long sensorId,LocalDateTime now) {

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
        eventLog.setStartTime(now); // Встановлюємо час початку

        return logRepository.save(eventLog);
    }

    public AlarmLog createAlarmLog(Long sessionId, String type, boolean status,LocalDateTime time){
        AlarmLog alarmLog = new AlarmLog();
        SessionDB sessionDB = sessionRepository.findById(sessionId).orElseThrow( );
        alarmLog.setSession(sessionDB);
        alarmLog.setType(type);
        alarmLog.setTime(time);
        alarmLog.setStatus(status);

        return alarmRepository.save(alarmLog);
    }

    public void createLog(List<EventLogger.SensorLog> logsInfo, Long sessionId){
        for(EventLogger.SensorLog log: logsInfo){
            if(log.sensorDetails() instanceof Sensor){
                createEventLog(((Sensor) log.sensorDetails()).getID(), log.currentTime());
            }else{
                createAlarmLog(sessionId,log.sensorDetails().getClass().getSimpleName(),log.activated(),log.currentTime());
            }
        }
    }



    public List<EventLog> getEventLogsBySensorId(long sensorId){
        return logRepository.findBySensor_SensorId(sensorId);
    }

    public List<SensorLog> getEventLogs(Long sessionId, Long floorId, Long roomId, String sensorType) throws Exception {

        Specification<EventLog> specification = EventLogSpecification.withFilters(sessionId, floorId, roomId, sensorType);
        System.out.println("a");
        List<EventLog> eventLogList = logRepository.findAll(specification);
        List<SensorLog> sensorLogs = new ArrayList<>();
        for(EventLog log:eventLogList){
            System.out.println("b");
            sensorLogs.add(EventLogMapper.eventLogToSensorLog(log));
            System.out.println("c");
        }
        return sensorLogs;
    }


}
