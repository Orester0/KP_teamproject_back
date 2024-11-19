package org.example.securitysystem.model.entity.security_system.alarms;

public class SpeakersAlarm extends AlarmSystem {
    @Override
    public void activateAlarm() {
//        System.out.println("Speakers Light Activated");
        securityEventManager.securityNotify("Speakers", this);
    }
}
