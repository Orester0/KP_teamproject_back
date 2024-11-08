package org.example.securitysystem.model.entity.security_system.sensors;

public class Camera extends Sensor {

    @Override
    public void detect() throws Exception {
        System.out.println("Strange Object Detected");
        securityMediator.notify(this, "Strange Object");
        securityEventManager.securityNotify("Strange Object", this);

    }
}
