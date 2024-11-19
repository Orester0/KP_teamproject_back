package org.example.securitysystem.model.entity.security_system.sensors;

public class TemperatureSensor extends Sensor {

    public TemperatureSensor() {
        super("TemperatureSensor");
    }

    @Override
    public void detect() throws Exception {
        System.out.println("High Temperature Detected!");
        securityMediator.notify(this, "High Temperature");
        securityEventManager.securityNotify("High Temperature", this);

    }
}
