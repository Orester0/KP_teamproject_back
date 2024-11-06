package org.example.securitysystem.model.entity.security_system.sensors;

public class Microphone extends Sensor {

    @Override
    public void detect() throws Exception {
        System.out.println("Heard Strange Sounds");
        securityMediator.notify(this, "Strange Sounds");
    }
}
