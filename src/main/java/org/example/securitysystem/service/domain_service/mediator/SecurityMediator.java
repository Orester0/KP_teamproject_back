package org.example.securitysystem.service.domain_service.mediator;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityMediator implements SecuritySystemMediator {
    private final Map<String, List<ISecurityColleague>> colleagues = new HashMap<>();

    @Override
    public void notify(ISecurityColleague sender, String event) throws Exception {
        List<ISecurityColleague> sirens, speakers;
        switch (event) {
            case "MotionSensor", "Camera" -> {
                sirens = colleagues.get("Siren");
                for (ISecurityColleague siren : sirens) {
                    ((AlarmSystem) siren).activateAlarm();
                }
            }
            case "Microphone" -> {
                speakers = colleagues.get("Speakers");
                for (ISecurityColleague speaker : speakers) {
                    ((AlarmSystem) speaker).activateAlarm();
                }
            }
            case "TemperatureSensor" -> {
                speakers = colleagues.get("FireExtinguishing");
                for (ISecurityColleague speaker : speakers) {
                    ((AlarmSystem) speaker).activateAlarm();
                }
            }
            default -> throw new Exception("Unknown Event");
        }
    }

    @Override
    public void register(ISecurityColleague colleague, String type) {
        colleagues.computeIfAbsent(type, k -> new ArrayList<>()).add(colleague);
    }
}
