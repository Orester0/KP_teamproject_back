package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.room.Room;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoomFactory {

    private static final Map<String, Class<? extends Room>> roomRegistry = new HashMap<>();

    static {

        Set<Class<? extends Room>> roomClasses = findSubclasses(Room.class);
        for (Class<? extends Room> roomClass : roomClasses) {
            try {
                String roomType = roomClass.getSimpleName();
                roomRegistry.put(roomType, roomClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Set<Class<? extends Room>> findSubclasses(Class<Room> superClass) {
        Reflections reflections = new Reflections("org.example.securitysystem.model.entity.room");

        return reflections.getSubTypesOf(superClass);
    }

    public static Room createRoom(String roomType) throws Exception {

        Class<? extends Room> roomClass = roomRegistry.get(roomType);

        if (roomClass != null) {
            try {
                return roomClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {

                throw new Exception("Error creating room of type " + roomType, e);
            }
        } else {

            throw new Exception("Unknown room type: " + roomType);
        }
    }
}

