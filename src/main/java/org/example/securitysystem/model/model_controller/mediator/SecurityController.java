package org.example.securitysystem.model.model_controller.mediator;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;

import java.util.HashMap;
import java.util.Map;

public class SecurityController implements SecuritySystemMediator {
    private final Map<String, SecurityColleague> colleagues = new HashMap<>();

    @Override
    public void notify(SecurityColleague sender, String event) {
        if (event.equals("Motion Detected")) {
            ((AlarmSystem)colleagues.get("AlarmSystem")).activateAlarm();
        } else {
            System.out.println("aaa");
        }
    }

    @Override
    public void register(SecurityColleague colleague, String type) {
        colleagues.put(type, colleague);
    }
}
