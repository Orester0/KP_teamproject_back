package org.example.securitysystem;

import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.model_controller.Linker;
import org.example.securitysystem.model.model_controller.mediator.SecurityController;
import org.example.securitysystem.model.model_controller.observer.SecurityEventManager;
import org.example.securitysystem.model.model_controller.robber_simulator.RobberSimulator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SecuritySystemApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SecuritySystemApplication.class, args);


        Building building = new Building(1, 100);

        building.buildOfficeFloor();
        building.finalizeBuilding();


        SecurityController securityController = new SecurityController();
        SecurityEventManager securityEventManager = new SecurityEventManager();

        Linker l = new Linker(building, securityController, securityEventManager);
        l.link();

        RobberSimulator rb = new RobberSimulator(building);

        rb.startSimulation(1);


        Thread.sleep(5000);
        rb.stopSimulation();


        System.out.println(l.getBuffer());





    }
}