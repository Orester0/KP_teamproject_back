package org.example.securitysystem;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Office;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SecuritySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuritySystemApplication.class, args);
        System.out.println("Hello World!");
    }
}
