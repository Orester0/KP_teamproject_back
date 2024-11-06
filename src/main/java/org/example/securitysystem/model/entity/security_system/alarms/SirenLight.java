package org.example.securitysystem.model.entity.security_system.alarms;

public class SirenLight extends AlarmSystem{
    @Override
    public void activateAlarm() {
        System.out.println("Siren Light Activated");
    }
}
