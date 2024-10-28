package org.example.securitysystem.model.model_controller.observer;

import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.model_controller.observer.listener.SecurityEventListener;
import org.springframework.jdbc.core.SqlReturnType;

import java.util.List;

public class SecurityEventManager {
    private List<Sensor> listener;

    public void subscribe(SecurityEventListener listener) { }
    public void unsubscribe(SecurityEventListener listener) { }
    public void securityNotify (String eventType, SqlReturnType sensorsDetails) { }
}
