package org.example.securitysystem.model.entity.security_system.sensors;

import lombok.Data;
import lombok.Getter;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;

@Data
public abstract class Sensor implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;
    protected SecurityEventManager securityEventManager;

    protected String HashID;
    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public void setEventManager(SecurityEventManager eventManager) { securityEventManager = eventManager;}

    public abstract  void detect() throws Exception; //цікаво чи розділити на два методи
}
