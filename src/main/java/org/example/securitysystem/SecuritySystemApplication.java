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

        Building building = new Building(3, 100);
        building.buildOfficeFloor();
        building.buildOfficeFloor();
        building.buildOfficeFloor();
        building.finalizeBuilding();

        Gson gson = new Gson();

        for(Floor floor : building.getFloors()) {
            System.out.println(gson.toJson(floor));
            for(Room room : floor.getRooms()) {
                System.out.println(gson.toJson(room));
                for(Sensor sensor : room.getSensors()) {
                    System.out.println(gson.toJson(sensor));
                }
            }
        }
        System.out.println(gson.toJson(building));
    }
}
