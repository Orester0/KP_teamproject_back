package org.example.securitysystem.model.entity.security_system.alarms;

public class SpeakersAlarm extends AlarmSystem {
    @Override
    public void activateAlarm() throws Exception {
        securityEventManager.securityNotify("SpeakersON", this);
        Thread.sleep(5000);
        deactivateAlarm();
    }

    @Override
    public void deactivateAlarm() throws Exception {
        isActive = false;
        securityEventManager.securityNotify("SpeakersOFF", this);
    }
}
