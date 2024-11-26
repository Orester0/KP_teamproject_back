package org.example.securitysystem.mappers;

import org.example.securitysystem.model.dto.SensorLog;
import org.example.securitysystem.model.db_models.EventLog;

public class EventLogMapper {
    public static SensorLog eventLogToSensorLog(EventLog eventLog) throws Exception {
       SensorLog sensorLog = new SensorLog();
       sensorLog.setSensorDetails(SensorMapper.SensorDBToSensor(eventLog.getSensor()));
       sensorLog.setActivated(true);
       sensorLog.setCurrentTime(eventLog.getStartTime().toString());
       return sensorLog;
    }
}
