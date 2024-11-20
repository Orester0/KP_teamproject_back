package org.example.securitysystem.model.entity.security_system.alarms;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;

public abstract class AlarmSystem implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;
    protected SecurityEventManager securityEventManager;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public void setEventManager(SecurityEventManager eventManager) { securityEventManager = eventManager;}
    public abstract void activateAlarm() throws Exception;
}
