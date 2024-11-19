package org.example.securitysystem.model.entity.security_system.alarms;

public class SirenLight extends AlarmSystem{
    @Override
    public void activateAlarm() throws Exception {
//        System.out.println("Siren Light Activated");
        securityEventManager.securityNotify("Siren", this);
    }
}
