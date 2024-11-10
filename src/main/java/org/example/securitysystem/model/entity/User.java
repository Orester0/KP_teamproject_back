package org.example.securitysystem.model.entity;
import org.example.securitysystem.model.entity.building.Building;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class User {
    private Long id;
    private String username;
    private Building building;


    public User(String username) {
        this.username = username;
    }
}