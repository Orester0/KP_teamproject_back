package org.example.securitysystem.model.entity.room;

import lombok.NoArgsConstructor;
import org.example.securitysystem.config.building_config.SecurityConfig;
import org.example.securitysystem.model.entity.security_system.sensors.*;
@NoArgsConstructor
public class Office extends Room {
    public Office(double area, int amountOfPorts) {
        super(area, amountOfPorts, "Office");
    }

    @Override
    public void calculateSensor() {
        int cameras = Math.max(1, (int) (area / SecurityConfig.OFFICE_CAMERA_AREA_PER_SENSOR));
        int microphones = Math.max(1, (int) (area / SecurityConfig.OFFICE_MICROPHONE_AREA_PER_SENSOR));
        int motionSensors = Math.max(1, amountOfPorts / SecurityConfig.OFFICE_MOTION_SENSOR_PORTS_PER_SENSOR);
        int temperatureSensors = Math.max(1, (int) (area / SecurityConfig.OFFICE_TEMPERATURE_AREA_PER_SENSOR));

        for (int i = 0; i < cameras; i++) {
            Camera camera = new Camera();
            addSensor(camera);
        }
        for (int i = 0; i < microphones; i++) {
            Microphone microphone = new Microphone();
            addSensor(microphone);
        }
        for (int i = 0; i < motionSensors; i++) {
            MotionSensor motionSensor = new MotionSensor();
            addSensor(motionSensor);
        }
        for (int i = 0; i < temperatureSensors; i++) {
            TemperatureSensor temperatureSensor = new TemperatureSensor();
            addSensor(temperatureSensor);
        }
    }
}

