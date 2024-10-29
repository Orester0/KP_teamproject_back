package org.example.securitysystem.model.model_controller.builder;

import org.example.securitysystem.model.entity.building.Floor;

public interface FloorBuilder {
    void buildWC();
    void buildDiningRoom();
    void buildLivingRoom();
    void buildOffice();
    void buildHall();
    void buildKitchen();
    Floor getFloor();
}

