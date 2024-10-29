package org.example.securitysystem.model.model_controller.builder;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.*;

public class OfficeFloorBuilder implements FloorBuilder {
    private double floorArea;
    private Floor floor;

    public OfficeFloorBuilder(double floorArea) {
        this.floorArea = floorArea;
        this.floor = new Floor();
    }

    @Override
    public void buildWC() {
        floor.addRoom(new WC(4.0, 1));
    }

    @Override
    public void buildDiningRoom() {
        floor.addRoom(new DiningRoom(10.0, 2));
    }

    @Override
    public void buildLivingRoom() {
        floor.addRoom(new LivingRoom(0.0, 0));
    }

    @Override
    public void buildOffice() {
        floor.addRoom(new Office(30.0, 6));
    }

    @Override
    public void buildHall() {
        floor.addRoom(new Hall(8.0, 2));
    }

    @Override
    public void buildKitchen() {
        floor.addRoom(new Kitchen(5.0, 1));
    }

    @Override
    public Floor getFloor() {
        return floor;
    }
}
