package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.db_models.SensorDB;

public class SensorMapper {

    public static Sensor SensorDBToSensor(SensorDB sensorDB) throws Exception{
        Sensor sensor  = SensorFactory.createSensor(sensorDB.getType());
        sensor.setID(sensorDB.getSensorId());
        sensor.setSensorType(sensorDB.getType());
        return  sensor;
    }

    public static SensorDB SensorToSensorDB(Sensor sensor) {
        if (sensor == null) {
            return null;
        }

        SensorDB sensorDB = new SensorDB();

        sensorDB.setType(sensor.getClass().getSimpleName());


        return sensorDB;
    }
}
