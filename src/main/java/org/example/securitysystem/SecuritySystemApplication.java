package org.example.securitysystem;

import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
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
            building.buildDefaultFloor();
            building.buildHostelFloor();
            building.buildOfficeFloor();
            building.setSensors();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        List<Floor> floors = building.getFloors();
        Floor firstFloor = floors.get(0);
        List<Sensor> firstFloorSensors = firstFloor.getSensors();
        for(Sensor sensor : firstFloorSensors)
        {
            System.out.println(sensor);
        }
        System.out.println(firstFloorSensors.size());
        System.out.println("End of work");
    }

}
