package org.example.securitysystem.model.entity.room;

import lombok.Getter;
import org.example.securitysystem.model.entity.security_system.sensors.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    @Getter
    protected double area;
    @Getter
    protected int amountOfPorts;
    @Getter
    protected List<Sensor> sensors = new ArrayList<>();

    public Room(double area, int amountOfPorts) {
        this.area = area;
        this.amountOfPorts = amountOfPorts;
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public abstract void calculateSensor();

}

