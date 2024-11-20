package org.example.securitysystem.model.model_controller;

import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.alarms.SirenLight;
import org.example.securitysystem.model.entity.security_system.alarms.SpeakersAlarm;
import org.example.securitysystem.model.model_controller.mediator.SecurityController;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;
import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
import org.example.securitysystem.model.model_controller.observer.listener.SecurityEventListener;

public class Linker {
    Building b;
    SecurityController sc;
    SecurityEventManager sem;
    SecurityEventListener el;

    public Linker(Building b, SecurityController sc, SecurityEventManager sem) {
        this.b = b;
        this.sc = sc;
        this.sem = sem;

        AlarmSystem siren = new SirenLight();
        AlarmSystem speakers = new SpeakersAlarm();

        sc.register(siren, "Siren");
        sc.register(speakers, "Speakers");
        siren.setEventManager(sem);
        speakers.setEventManager(sem);

        el = new EventLogger();
        sem.subscribe(el);
    }

    public void link() {
        for (var floor : b.getFloors()) {
            for (var sensor : floor.getSensors()) {
                sc.register(sensor, sensor.getClass().getSimpleName());
                sensor.setMediator(sc);
                sensor.setEventManager(sem);
            }
        }
    }

    public String getBuffer() {
        return ((EventLogger)el).getBuffer();
    }


}
