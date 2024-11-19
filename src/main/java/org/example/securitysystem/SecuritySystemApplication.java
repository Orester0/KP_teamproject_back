package org.example.securitysystem;

import org.example.securitysystem.model.entity.building.Building;
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
        Building building = new Building(3, 100);
        try
        {
            building.buildOfficeFloor();
            building.buildOfficeFloor();
            building.buildOfficeFloor();
            building.setSensors();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }


        SecurityEventManager eventManager = new SecurityEventManager();
        SecurityController securityController = new SecurityController();


        Linker l = new Linker(building, securityController, eventManager);
        l.link();

        System.out.println("Starting simulation");
        RobberSimulator simulator = new RobberSimulator(building);
        simulator.startSimulation(10);

        Thread.sleep(1000);
        simulator.stopSimulation();
        System.out.println("Stopping simulation");

        System.out.println(l.getBuffer());
    }
}
