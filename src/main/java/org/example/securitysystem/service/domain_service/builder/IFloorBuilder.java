package org.example.securitysystem.service.domain_service.builder;

import org.example.securitysystem.model.entity.building.Floor;

public interface IFloorBuilder {

    IFloorBuilder buildWC();
    IFloorBuilder buildDiningRoom();
    IFloorBuilder buildLivingRoom();
    IFloorBuilder buildOffice();
    IFloorBuilder buildHall();
    IFloorBuilder buildKitchen();

    Floor finalizeFloor();

    Floor getFloor();
}

