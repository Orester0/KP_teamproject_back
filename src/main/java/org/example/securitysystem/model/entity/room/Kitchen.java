package org.example.securitysystem.model.entity.room;

import org.example.securitysystem.config.SecurityConfig;
import org.example.securitysystem.model.entity.security_system.sensors.*;

public class Kitchen extends Room {
    public Kitchen(double area, int amountOfPorts) {
        super(area, amountOfPorts);
    }

    @Override
    public void calculateSensor() {
        int cameras = Math.max(1, (int) (area / SecurityConfig.KITCHEN_CAMERA_AREA_PER_SENSOR));
        int microphones = Math.max(1, (int) (area / SecurityConfig.KITCHEN_MICROPHONE_AREA_PER_SENSOR));
        int motionSensors = Math.max(1, amountOfPorts / SecurityConfig.KITCHEN_MOTION_SENSOR_PORTS_PER_SENSOR);
        int temperatureSensors = Math.max(1, (int) (area / SecurityConfig.KITCHEN_TEMPERATURE_AREA_PER_SENSOR));

        for (int i = 0; i < cameras; i++) addSensor(new Camera());
        for (int i = 0; i < microphones; i++) addSensor(new Microphone());
        for (int i = 0; i < motionSensors; i++) addSensor(new MotionSensor());
        for (int i = 0; i < temperatureSensors; i++) addSensor(new TemperatureSensor());
    }
}

