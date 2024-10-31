package org.example.securitysystem.model.entity.security_system.alarms;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;

public abstract class AlarmSystem implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
        System.out.println("added");
    }

    public abstract void activateAlarm();
}
