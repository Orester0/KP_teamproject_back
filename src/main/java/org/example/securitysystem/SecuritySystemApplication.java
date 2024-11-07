package org.example.securitysystem;

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
        List<Floor> floors = building.getFloors();
        for(Floor floor : floors){
            List<Room> rooms = floor.getRooms();
            for(Room room : rooms)
            {
                for(Sensor sensor : room.getSensors()){
                    System.out.println(sensor.getHashID());
                }
            }
        }
        System.out.println("End of work");
    }
}
