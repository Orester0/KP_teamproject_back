package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.models_db.SensorDB;

public class SensorMapper {

    public static Sensor SensorDBToSensor(SensorDB sensorDB) throws Exception{
        return  SensorFactory.createSensor(sensorDB.getType());
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
