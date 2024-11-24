package org.example.securitysystem.model.entity.security_system.alarms;

public class SirenLight extends AlarmSystem{
    @Override
    public void activateAlarm() throws Exception {
        securityEventManager.securityNotify("SirenON", this);
        Thread.sleep(1000);
        deactivateAlarm();
    }

    @Override
    public void deactivateAlarm() throws Exception {
        isActive = false;
        securityEventManager.securityNotify("SirenOFF", this);
    }
}
