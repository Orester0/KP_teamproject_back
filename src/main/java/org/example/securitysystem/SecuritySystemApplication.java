package org.example.securitysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecuritySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuritySystemApplication.class, args);
        System.out.println("Start of work");
        System.out.println("End of work");
    }
}
