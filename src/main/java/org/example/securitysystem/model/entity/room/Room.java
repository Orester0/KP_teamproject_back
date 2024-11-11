package org.example.securitysystem.model.entity.room;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.example.securitysystem.model.entity.security_system.sensors.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Room implements Serializable {
    protected double area;
    protected int amountOfPorts;
    private String roomId;

    protected List<Sensor> sensors = new ArrayList<>();

    public Room(double area, int amountOfPorts) {
        this.area = area;
        this.amountOfPorts = amountOfPorts;
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public abstract void calculateSensor();


    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "area=" + area +
                ", amountOfPorts=" + amountOfPorts +
                ", sensors=" + sensors +
                '}';
    }


}

