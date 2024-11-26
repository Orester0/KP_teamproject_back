package org.example.securitysystem;

import org.example.securitysystem.service.implementations.LogService;
import org.example.securitysystem.service.implementations.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

//        // Використання sessionService
//       Session session =  sessionService.createSession("new4") ;
//        session.setBuilding(asd);
//        sessionService.updateSession(session);
//        System.out.println("Session created!");
    }
}
