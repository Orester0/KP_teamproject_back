package org.example.securitysystem.model.entity.security_system.alarms;

public class FireExtinguishing extends AlarmSystem{
    @Override
    public void activateAlarm() throws Exception {
        securityEventManager.securityNotify("FireExtinguishingON", this);
        Thread.sleep(1000);
        deactivateAlarm();
    }

    @Override
    public void deactivateAlarm() throws Exception {
        isActive = false;
        securityEventManager.securityNotify("FireExtinguishingOFF", this);
    }

    @Override
    public Boolean isAlarmActive() throws Exception {
        return isActive;
    }
}
