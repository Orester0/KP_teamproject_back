package org.example.securitysystem.model.entity;
import com.google.gson.annotations.Expose;
import org.example.securitysystem.model.entity.building.Building;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Session {
    @Expose
    private Long id;
    @Expose
    private String name;

    private transient Building building;


    public Session(String name) {
        this.name = name;
    }
}