package org.example.securitysystem.service.domain_service.observer.listener;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.ISecurityColleague;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventLogger implements SecurityEventListener {

    public static List<SensorLog> objectList = new ArrayList<>();
    public static List<SensorLog> objectList2 = new ArrayList<>();

    @Override
    public synchronized void update(String eventType, ISecurityColleague sensorDetails) {
        boolean activated = true;
        LocalDateTime time  = LocalDateTime.now();

        if (eventType.endsWith("FF")) activated = false;

        SensorLog sensorLog = new SensorLog(sensorDetails, activated, time);

        objectList.add(sensorLog);
        objectList2.add(sensorLog);
    }

    public synchronized List<SensorLog> getObjectList() {
        return objectList;
    }

    public synchronized void clearObjectList() {
        objectList.clear();
    }

    public synchronized List<SensorLog> getObjectList2() {
        return objectList2;
    }

    public synchronized void clearObjectList2() {
        objectList2.clear();
    }

    public record SensorLog(ISecurityColleague sensorDetails, boolean activated, LocalDateTime currentTime) {}
}