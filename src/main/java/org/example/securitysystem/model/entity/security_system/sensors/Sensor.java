package org.example.securitysystem.model.entity.security_system.sensors;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;

public abstract class Sensor implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public abstract  void detect();
}
