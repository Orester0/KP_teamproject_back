package org.example.securitysystem.model.model_controller.builder;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.*;

public class HostelFloorBuilder implements FloorBuilder {
    private double floorArea;

    private Floor floor;

    public HostelFloorBuilder(double floorArea) {
        this.floorArea = floorArea;
        this.floor = new Floor();
    }

    @Override
    public void buildWC() {
        floor.addRoom(new WC(5.0, 2));
    }

    @Override
    public void buildDiningRoom() {
        floor.addRoom(new DiningRoom(15.0, 4));
    }

    @Override
    public void buildLivingRoom() {
        floor.addRoom(new LivingRoom(20.0, 3));
    }

    @Override
    public void buildOffice() {
        floor.addRoom(new Office(10.0, 3));
    }

    @Override
    public void buildHall() {
        floor.addRoom(new Hall(10.0, 2));
    }

    @Override
    public void buildKitchen() {
        floor.addRoom(new Kitchen(12.0, 3));
    }

    @Override
    public Floor getFloor() {
        return floor;
    }
}
