package org.example.securitysystem;

import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.alarms.SirenLight;
import org.example.securitysystem.model.entity.security_system.alarms.SpeakersAlarm;
import org.example.securitysystem.model.entity.security_system.sensors.*;
import org.example.securitysystem.model.model_controller.mediator.SecurityController;
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
//        System.out.println(firstFloorSensors.size());
//        System.out.println("End of work");

        AlarmSystem siren = new SirenLight();
        AlarmSystem speakers = new SpeakersAlarm();

        SecurityController securityController = new SecurityController();
        Sensor motionSensor = new MotionSensor(); //Motion Detected
        Sensor cameraSensor = new Camera(); //Strange Object
        Sensor microphoneSensor = new Microphone(); //Strange Sounds
        Sensor temperatureSensor = new TemperatureSensor(); //High Temperature

        motionSensor.setMediator(securityController);
        cameraSensor.setMediator(securityController);
        microphoneSensor.setMediator(securityController);
        temperatureSensor.setMediator(securityController);

        securityController.register(motionSensor, "MotionSensor");
        securityController.register(cameraSensor, "CameraSensor");
        securityController.register(microphoneSensor, "MicrophoneSensor");
        securityController.register(temperatureSensor, "TemperatureSensor");

        securityController.register(siren, "Siren");
        securityController.register(speakers, "Speakers");

        motionSensor.detect();
        System.out.println("----------------------------------");

        cameraSensor.detect();
        System.out.println("----------------------------------");

        microphoneSensor.detect();
        System.out.println("----------------------------------");

        temperatureSensor.detect();
        System.out.println("----------------------------------");

    }
}
