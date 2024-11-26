package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.db_models.FloorDB;

public class FloorMapper {
    public static Floor FloorDBToFloor(FloorDB floor){
        Floor newFloor  = new Floor();
        newFloor.setID(String.valueOf(floor.getFloorId()));
        return newFloor;
    }
    public static FloorDB FloorToFloorDB(Floor floor) {
        FloorDB newFloorDB = new FloorDB();
        newFloorDB.setType("default");  // Ти можеш вказати тип в залежності від необхідності
        newFloorDB.setFloorNumber(1);   // Тут також можна вказати номер поверху за умовами твоїх даних

        return newFloorDB;
    }
}
