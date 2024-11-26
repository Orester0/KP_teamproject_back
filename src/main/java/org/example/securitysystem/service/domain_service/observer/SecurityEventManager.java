package org.example.securitysystem.service.domain_service.observer;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.service.domain_service.observer.listener.SecurityEventListener;

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
    public void securityNotify (String eventType, ISecurityColleague sensorsDetails) {
        for (SecurityEventListener listener : listeners) {
            listener.update(eventType, sensorsDetails);
        }
    }
}
