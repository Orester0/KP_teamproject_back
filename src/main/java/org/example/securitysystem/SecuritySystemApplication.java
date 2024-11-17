package org.example.securitysystem;

import com.google.gson.Gson;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SecuritySystemApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SecuritySystemApplication.class, args);
        System.out.println("Hello World!");

        Gson gson = new Gson();

        Building asd = new Building(1, 20);

        asd.buildOfficeFloor();

        asd.finalizeBuilding();
        for(Room room : asd.getFloors().get(0).getRooms()){
            System.out.println(gson.toJson(room));
        }


    }
}
