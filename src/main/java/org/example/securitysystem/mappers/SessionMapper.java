package org.example.securitysystem.mappers;


import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.models_db.FloorDB;
import org.example.securitysystem.model.models_db.RoomDB;
import org.example.securitysystem.model.models_db.SensorDB;
import org.example.securitysystem.model.models_db.SessionDB;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionMapper {

    public static Session mapToSession(SessionDB sessionDB) throws Exception {
        if (sessionDB == null) {
            return null;
        }

        Session session = new Session(sessionDB.getName());
        session.setId(sessionDB.getSessionId());

        Building building = new Building(sessionDB.getHeightInFloors(), sessionDB.getFloorArea());
        for (FloorDB floorDB : sessionDB.getFloors()) {
            Floor newFloor = FloorMapper.FloorDBToFloor(floorDB);

            for (RoomDB roomDB : floorDB.getRooms()) {

                Room newRoom = RoomMapper.RoomDbToRoom(roomDB);
                for(SensorDB sensorDb: roomDB.getSensors()){
                    Sensor newSensor = SensorMapper.SensorDBToSensor(sensorDb);
                    newRoom.addSensor(newSensor);
                }
                newFloor.addRoom(newRoom);
            }
            building.addFloor(newFloor);
        }
        session.setBuilding(new Building(sessionDB.getHeightInFloors(), sessionDB.getFloorArea()));

        return session;
    }

    public static SessionDB mapToSessionDB(Session session) throws Exception {
        if (session == null) {
            return null;
        }

        SessionDB sessionDB = new SessionDB();
        if(session.getId()!=null){
        sessionDB.setSessionId(session.getId());}
        sessionDB.setName(session.getName()); // Назва сесії обов'язкова, її можна встановлювати без перевірки

        // Перевіряємо на null для building
        if (session.getBuilding() != null) {
            Building building = session.getBuilding();

            // Перевіряємо висоту поверхів
            if (building.getHeightInFloors() != 0) {
                sessionDB.setHeightInFloors(building.getHeightInFloors());
            } else {
                System.out.println("HeightInFloors is null.");
            }

            // Перевіряємо площу поверху
            if (building.getFloorArea() != 0) {
                sessionDB.setFloorArea(building.getFloorArea());
            } else {
                System.out.println("FloorArea is null.");
            }

            // Перевірка на наявність поверхів і обробка їх
            if (building.getFloors() != null) {
                for (Floor floor : building.getFloors()) {
                    FloorDB floorDB = FloorMapper.FloorToFloorDB(floor);

                    if (floor.getRooms() != null) {
                        for (Room room : floor.getRooms()) {
                            RoomDB roomDB = RoomMapper.RoomToRoomDB(room);

                            if (room.getSensors() != null) {
                                for (Sensor sensor : room.getSensors()) {
                                    SensorDB sensorDB = SensorMapper.SensorToSensorDB(sensor);
                                    sensorDB.setRoom(roomDB); // Зв'язуємо сенсор з кімнатою
                                    roomDB.getSensors().add(sensorDB);
                                }
                            }

                            roomDB.setFloor(floorDB); // Зв'язуємо кімнату з поверхом
                            floorDB.getRooms().add(roomDB);
                        }
                    }

                    floorDB.setSession(sessionDB); // Зв'язуємо поверх із сесією
                    sessionDB.getFloors().add(floorDB);
                }
            }
        } else {
            System.out.println("Building is null.");
        }

        return sessionDB;
    }


}
