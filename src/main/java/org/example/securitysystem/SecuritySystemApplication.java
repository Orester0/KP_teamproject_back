package org.example.securitysystem;

import com.google.gson.Gson;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.models_db.EventLog;
import org.example.securitysystem.service.LogService;
import org.example.securitysystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SecuritySystemApplication implements CommandLineRunner {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private LogService logService;

    public static void main(String[] args) {
        SpringApplication.run(SecuritySystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World!");

        Gson gson = new Gson();

        Building asd = new Building(3, 99);

        asd.buildOfficeFloor();
        asd.buildDefaultFloor();
        asd.buildHostelFloor();

        asd.finalizeBuilding();

        for (Floor floor : asd.getFloors()) {
            System.out.println(floor.getRooms().size());
            for (Room room : floor.getRooms()) {
                System.out.println(gson.toJson(room));
            }
        }

       List< EventLog > e = logService.getEventLogs(null,null,null,"Microphone");
      for(EventLog ev: e){
          System.out.println(ev.getSensor().getType());
      }
//        // Використання sessionService
//       Session session =  sessionService.createSession("new4") ;
//        session.setBuilding(asd);
//        sessionService.updateSession(session);
//        System.out.println("Session created!");
    }
}
