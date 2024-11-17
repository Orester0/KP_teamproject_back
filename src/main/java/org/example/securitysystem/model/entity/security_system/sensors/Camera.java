package org.example.securitysystem.model.entity.security_system.sensors;

public class Camera extends Sensor {

    public Camera() {
        super("Camera");
    }

    @Override
    public void detect() throws Exception {
//        System.out.println("Strange Object Detected");
        securityEventManager.securityNotify("Camera", this);
        securityMediator.notify(this, "Camera");

    }
}
