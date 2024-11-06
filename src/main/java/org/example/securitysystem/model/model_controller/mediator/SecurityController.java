package org.example.securitysystem.model.model_controller.mediator;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityController implements SecuritySystemMediator {
    private final Map<String, List<SecurityColleague>> colleagues = new HashMap<>();

    @Override
    public void notify(SecurityColleague sender, String event) throws Exception {
        List<SecurityColleague> sirens, speakers;
        switch (event) {
            case "Motion Detected":
                sirens = colleagues.get("Siren");
                speakers = colleagues.get("Speakers");
                for (SecurityColleague siren : sirens) {
                    ((AlarmSystem) siren).activateAlarm();
                }
                for (SecurityColleague speaker : speakers) {
                    ((AlarmSystem) speaker).activateAlarm();
                }
                break;

            case "Strange Object":
                sirens = colleagues.get("Siren");
                for (SecurityColleague siren : sirens) {
                    ((AlarmSystem) siren).activateAlarm();
                }
                break;
            case "Strange Sounds":
                speakers = colleagues.get("Speakers");
                for (SecurityColleague speaker : speakers) {
                    ((AlarmSystem) speaker).activateAlarm();
                }
                break;
            case "High Temperature":
                sirens = colleagues.get("Siren");
                speakers = colleagues.get("Speakers");
                for (SecurityColleague speaker : speakers) {
                    ((AlarmSystem) speaker).activateAlarm();
                }
                for (SecurityColleague siren : sirens) {
                    ((AlarmSystem) siren).activateAlarm();
                }
                break;
            default:
                throw new Exception("Unknown Event");
        }
    }

    @Override
    public void register(SecurityColleague colleague, String type) {
        colleagues.computeIfAbsent(type, k -> new ArrayList<>()).add(colleague);
    }
}
