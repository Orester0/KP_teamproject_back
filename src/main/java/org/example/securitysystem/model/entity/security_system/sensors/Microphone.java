package org.example.securitysystem.model.entity.security_system.sensors;

public class Microphone extends Sensor {

    public Microphone() {
        super("Microphone");
    }

    @Override
    public void detect() throws Exception {
//        System.out.println("Heard Strange Sounds");
        securityEventManager.securityNotify("Microphone", this);
        securityMediator.notify(this, "Microphone");

    }
}
