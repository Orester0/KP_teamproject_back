package org.example.securitysystem.model.entity.building;

import org.example.securitysystem.model.entity.room.Room;

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
}
