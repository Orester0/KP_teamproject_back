package org.example.securitysystem.model.entity;
import lombok.RequiredArgsConstructor;
import org.example.securitysystem.model.entity.building.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private Building building;


    public User(String name) {
        this.id = Long.valueOf(UUID.randomUUID().toString());
        this.name = name;
    }
}