package org.example.securitysystem.model.entity.security_system.sensors;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;

import java.io.Serializable;

@Data
public abstract class Sensor implements ISecurityColleague, Serializable {
    protected SecuritySystemMediator securityMediator;
    protected SecurityEventManager securityEventManager;
    @Expose
    protected long ID;
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

    public abstract  void detect() throws Exception;



    public String getType() {
        return this.getType();
    }

}
