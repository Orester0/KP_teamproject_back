package org.example.securitysystem.model.entity.security_system.sensors;

public class MotionSensor extends Sensor {

    @Override
    public void detect() throws Exception {
        System.out.println("Motion Detected!!");
        securityMediator.notify(this, "Motion Detected");
        securityEventManager.securityNotify("Motion Detected", this);
    }
}
