package org.example.securitysystem.model.entity;
import org.example.securitysystem.model.entity.building.Building;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Session {
    private Long id;
    private String name;
    private Building building;


    public Session(String name) {
        this.name = name;
    }
}