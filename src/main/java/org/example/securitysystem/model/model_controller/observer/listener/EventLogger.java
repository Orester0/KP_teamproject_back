package org.example.securitysystem.model.model_controller.observer.listener;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class EventLogger implements SecurityEventListener {

    public static String buffer = "";

    @Override
    public synchronized void update(String eventType, SecurityColleague sensorDetails) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        buffer += sensorDetails.getClass().getSimpleName() + " was activated on " + now.format(formatter);

        if (sensorDetails instanceof Sensor sensor) {
            buffer += " and hash " + sensor.getID() + "\n";
        } else {
            buffer += "\n";
        }
    }

    public String getBuffer() {
        return buffer;
    }
}
