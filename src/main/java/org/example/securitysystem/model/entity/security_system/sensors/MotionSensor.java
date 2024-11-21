package org.example.securitysystem.model.entity.security_system.sensors;

public class MotionSensor extends Sensor {

    public MotionSensor() {
        super("MotionSensor");
    }

    @Override
    public void detect() throws Exception {
        securityEventManager.securityNotify("MotionSensor", this);
        securityMediator.notify(this, "MotionSensor");

    }
}
