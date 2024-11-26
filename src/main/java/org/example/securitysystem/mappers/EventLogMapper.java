package org.example.securitysystem.mappers;

import org.example.securitysystem.model.dto.SensorLog;
import org.example.securitysystem.model.models_db.EventLog;

public class EventLogMapper {
    public static SensorLog eventLogToSensorLog(EventLog eventLog)  {
        try{
        return new SensorLog(SensorMapper.SensorDBToSensor(eventLog.getSensor()),true,eventLog.getStartTime());} catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }
}
