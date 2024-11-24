package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.models_db.RoomDB;

public class RoomMapper {
    public static Room RoomDbToRoom(RoomDB room) throws Exception{
        Room newRoom = RoomFactory.createRoom(room.getType());
        room.setType(room.getType());
        room.setArea(room.getArea());
        room.setAmountOfPorts(room.getAmountOfPorts());
        return newRoom;
    }

    public static RoomDB RoomToRoomDB(Room room) throws Exception {
        RoomDB newRoomDB = new RoomDB();
        newRoomDB.setType(room.getRoomType());
        newRoomDB.setArea(room.getArea());
        newRoomDB.setAmountOfPorts(room.getAmountOfPorts());


        return newRoomDB;
    }
}
