package org.example.securitysystem.model.entity.security_system.sensors;

import lombok.Data;
import lombok.Getter;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;

@Data
public abstract class Sensor implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;

    protected String HashID;
    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public abstract  void detect();

}
