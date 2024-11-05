package org.example.securitysystem.model.entity.building;

import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Sensor> getSensors(){
        List<Sensor> sensors = new ArrayList<>();

        for(Room room : rooms){
            sensors.addAll(room.getSensors());
        }
        return sensors;
    }
}
