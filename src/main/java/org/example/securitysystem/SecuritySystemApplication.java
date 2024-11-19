package org.example.securitysystem;

import com.google.gson.Gson;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SecuritySystemApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SecuritySystemApplication.class, args);
        System.out.println("Hello World!");

        Gson gson = new Gson();

        Building asd = new Building(3, 99);

        asd.buildOfficeFloor();
        asd.buildDefaultFloor();
        asd.buildHostelFloor();

        asd.finalizeBuilding();

        for(Floor floor : asd.getFloors()) {
            System.out.println(floor.getRooms().size());
            for(Room room : floor.getRooms()){
                System.out.println(gson.toJson(room));
            }
        }



    }
}
