package org.example.securitysystem.model.model_controller.observer.listener;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;

public class EventLogger implements SecurityEventListener {
    @Override
    public void update(String eventType, SecurityColleague sensorDetails) {
        System.out.println("EventLogger: " + eventType + " detected by " + sensorDetails);
    }
}
