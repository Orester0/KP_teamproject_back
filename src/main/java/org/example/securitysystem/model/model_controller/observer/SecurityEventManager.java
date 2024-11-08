package org.example.securitysystem.model.model_controller.observer;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.model_controller.observer.listener.SecurityEventListener;
import org.springframework.jdbc.core.SqlReturnType;

import java.util.ArrayList;
import java.util.List;

public class SecurityEventManager {
    private final List<SecurityEventListener> listeners = new ArrayList<>();

    public void subscribe(SecurityEventListener listener) {
        listeners.add(listener);
    }
    public void unsubscribe(SecurityEventListener listener) {
        listeners.remove(listener);
    }
    public void securityNotify (String eventType, SecurityColleague sensorsDetails) {
        for (SecurityEventListener listener : listeners) {
            listener.update(eventType, sensorsDetails);
        }
    }
}
