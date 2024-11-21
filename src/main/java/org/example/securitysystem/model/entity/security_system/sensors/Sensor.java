package org.example.securitysystem.model.entity.security_system.sensors;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.model_controller.mediator.SecuritySystemMediator;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;

import java.io.Serializable;

@Data
public abstract class Sensor implements SecurityColleague, Serializable {
    protected SecuritySystemMediator securityMediator;
    protected SecurityEventManager securityEventManager;
    @Expose
    protected String ID;
    @Expose
    protected String SensorType;

    public Sensor(String SensorType) {
        this.SensorType = SensorType;
    }
    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public void setEventManager(SecurityEventManager eventManager) { securityEventManager = eventManager;}

    public abstract  void detect() throws Exception; //цікаво чи розділити на два методи

}
