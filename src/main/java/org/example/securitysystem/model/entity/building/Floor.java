package org.example.securitysystem.model.entity.building;

import com.google.gson.annotations.Expose;
import lombok.Data;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Floor implements Serializable {
    @Expose
    private List<Room> rooms = new ArrayList<>();
    @Expose
    protected String ID;
    @Expose
    protected int floorNumber;

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Sensor> getSensors(){
        List<Sensor> sensors = new ArrayList<>();

        for(Room room : rooms){
            sensors.addAll(room.getSensors());
        }
        return sensors;
    }
}
