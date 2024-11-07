package org.example.securitysystem.model.entity.security_system.sensors;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;

public abstract class Sensor implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;
    protected SecurityEventManager securityEventManager;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public void setEventManager(SecurityEventManager eventManager) { securityEventManager = eventManager;}

    public abstract  void detect() throws Exception; //цікаво чи розділити на два методи
}
