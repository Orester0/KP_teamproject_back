package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.db_models.RoomDB;

public class RoomMapper {
    public static Room RoomDbToRoom(RoomDB room) throws Exception{

        Room newRoom = RoomFactory.createRoom(room.getType());
      newRoom.setArea(room.getArea());
      newRoom.setAmountOfPorts(room.getAmountOfPorts());
      newRoom.setID(room.getRoomId());
      newRoom.setRoomType(room.getType());

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
