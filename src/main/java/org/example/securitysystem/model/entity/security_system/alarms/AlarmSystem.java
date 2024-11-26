package org.example.securitysystem.model.entity.security_system.alarms;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;

public abstract class AlarmSystem implements ISecurityColleague {
    protected SecuritySystemMediator securityMediator;
    protected SecurityEventManager securityEventManager;

    protected Boolean isActive = false;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public void setEventManager(SecurityEventManager eventManager) { securityEventManager = eventManager;}
    public abstract void activateAlarm() throws Exception;
    public abstract  void deactivateAlarm() throws  Exception;


}
