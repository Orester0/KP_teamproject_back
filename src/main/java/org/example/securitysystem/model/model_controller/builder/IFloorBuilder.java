package org.example.securitysystem.model.model_controller.builder;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;

public interface IFloorBuilder {
    double floorArea = 0;

    IFloorBuilder buildWC();
    IFloorBuilder buildDiningRoom();
    IFloorBuilder buildLivingRoom();
    IFloorBuilder buildOffice();
    IFloorBuilder buildHall();
    IFloorBuilder buildKitchen();

    Floor finalizeFloor();

    Floor getFloor();
}

