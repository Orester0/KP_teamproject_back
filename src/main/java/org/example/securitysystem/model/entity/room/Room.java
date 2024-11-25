package org.example.securitysystem.model.entity.room;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.securitysystem.model.entity.security_system.sensors.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public abstract class Room implements Serializable {
    @Expose
    protected double area;
    @Expose
    protected int amountOfPorts;
    @Expose
    protected long ID;
    @Expose
    protected String RoomType;

    protected List<Sensor> sensors = new ArrayList<>();

    public Room(double area, int amountOfPorts, String RoomType) {
        this.area = area;
        this.amountOfPorts = amountOfPorts;
        this.RoomType = RoomType;
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

