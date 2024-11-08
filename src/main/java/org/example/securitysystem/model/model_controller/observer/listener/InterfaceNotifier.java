package org.example.securitysystem.model.model_controller.observer.listener;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;

public class InterfaceNotifier implements SecurityEventListener {
    @Override
    public void update(String eventType, SecurityColleague sensorDetails) {
        System.out.println("Interface Notifier: " + eventType + " detected by " + sensorDetails);
    }
}
