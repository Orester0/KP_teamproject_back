package org.example.securitysystem.model.entity.security_system.sensors;

public class MotionSensor extends Sensor {

    @Override
    public void detect() {
        securityMediator.notify(this, "Motion Detected");
    }
}
