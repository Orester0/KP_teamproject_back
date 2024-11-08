package org.example.securitysystem.model.model_controller.observer.listener;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;

public interface SecurityEventListener {
    void update(String evenType, SecurityColleague sensorDetails);
}
