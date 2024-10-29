package org.example.securitysystem.model.model_controller.builder;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.*;

class DefaultFloorBuilder implements FloorBuilder {
    private double floorArea;

    private Floor floor;

    public DefaultFloorBuilder(double floorArea) {
        this.floorArea = floorArea;
        this.floor = new Floor();
    }

    @Override
    public void buildWC() {
        floor.addRoom(new WC(6.0, 2));
    }

    @Override
    public void buildDiningRoom() {
        floor.addRoom(new DiningRoom(10.0, 2));
    }

    @Override
    public void buildLivingRoom() {
        floor.addRoom(new LivingRoom(18.0, 4));
    }

    @Override
    public void buildOffice() {
        floor.addRoom(new Office(0.0, 0));  // Не потрібно
    }

    @Override
    public void buildHall() {
        floor.addRoom(new Hall(12.0, 3));
    }

    @Override
    public void buildKitchen() {
        floor.addRoom(new Kitchen(7.0, 2));
    }

    @Override
    public Floor getFloor() {
        return floor;
    }
}
