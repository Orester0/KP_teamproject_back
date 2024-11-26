package org.example.securitysystem.service.domain_service.observer.listener;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;

public interface SecurityEventListener {
    void update(String evenType, ISecurityColleague sensorDetails);
}
