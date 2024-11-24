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


    }
}
