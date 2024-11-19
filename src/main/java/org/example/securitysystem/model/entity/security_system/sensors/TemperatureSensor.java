package org.example.securitysystem.model.entity.security_system.sensors;

public class TemperatureSensor extends Sensor {

    @Override
    public void detect() throws Exception {
//        System.out.println("High Temperature Detected!");
        securityEventManager.securityNotify("TemperatureSensor", this);
        securityMediator.notify(this, "TemperatureSensor");


    }
}
