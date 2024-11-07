package org.example.securitysystem;

import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.alarms.SirenLight;
import org.example.securitysystem.model.entity.security_system.alarms.SpeakersAlarm;
import org.example.securitysystem.model.entity.security_system.sensors.*;
import org.example.securitysystem.model.model_controller.mediator.SecurityController;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;
import org.example.securitysystem.model.model_controller.observer.listener.EventLogger;
import org.example.securitysystem.model.model_controller.observer.listener.InterfaceNotifier;
import org.example.securitysystem.model.model_controller.observer.listener.SecurityEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SecuritySystemApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SecuritySystemApplication.class, args);

//        Building building = new Building(3, 100);
//        try
//        {
//            building.buildDefaultFloor();
//            building.buildHostelFloor();
//            building.buildOfficeFloor();
//            building.setSensors();
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//        List<Floor> floors = building.getFloors();
//        Floor firstFloor = floors.get(0);
//        List<Sensor> firstFloorSensors = firstFloor.getSensors();
//        for(Sensor sensor : firstFloorSensors)
//        {
//            System.out.println(sensor);
//        }
//
//        SecurityController securityController = new SecurityController();
//
//        for (Sensor sensor : firstFloorSensors) {
//            sensor.setMediator(securityController);
//
//            String sensorType;
//            if (sensor instanceof MotionSensor) {
//                sensorType = "MotionSensor";
//            } else if (sensor instanceof Camera) {
//                sensorType = "CameraSensor";
//            } else if (sensor instanceof Microphone) {
//                sensorType = "MicrophoneSensor";
//            } else if (sensor instanceof TemperatureSensor) {
//                sensorType = "TemperatureSensor";
//            } else {
//                continue; // Пропустити, якщо тип сенсора не визначений
//            }
//
//            securityController.register(sensor, sensorType);
//        }
//

//
//        firstFloorSensors.get(1).detect();

        SecurityController securityController = new SecurityController();
        SecurityEventManager eventManager = new SecurityEventManager();

        SecurityEventListener eventLogger = new EventLogger();
        SecurityEventListener eventNotifier = new InterfaceNotifier();
        eventManager.subscribe(eventLogger);
        eventManager.subscribe(eventNotifier);


        AlarmSystem siren = new SirenLight();
        AlarmSystem speakers = new SpeakersAlarm();

        securityController.register(siren, "Siren");
        securityController.register(speakers, "Speakers");


        Sensor motionSensor = new MotionSensor();
        motionSensor.setEventManager(eventManager);

        securityController.register(motionSensor, "Motion Detected");
        motionSensor.setMediator(securityController);

        motionSensor.detect();
    }
}

