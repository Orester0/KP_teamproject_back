package org.example.securitysystem.model.entity.building;

import org.example.securitysystem.model.model_controller.builder.FloorBuilder;

import java.util.ArrayList;
import java.util.List;

class Building {
    private List<Floor> floors = new ArrayList<>();
    private FloorBuilder floorBuilder;
    private double floorArea;
    private int amountOfFloors;

    public Building(int amountOfFloors) {
        this.amountOfFloors = amountOfFloors;
    }

    public void AssignFloorBuilder(FloorBuilder floorBuilder) {
        this.floorBuilder = floorBuilder;
    }

    public void buildFloor() {
        for (int i = 0; i < amountOfFloors; i++) {
            floorBuilder.buildWC();
            floorBuilder.buildDiningRoom();
            floorBuilder.buildLivingRoom();
            floorBuilder.buildOffice();
            floorBuilder.buildHall();
            floorBuilder.buildKitchen();
            floors.add(floorBuilder.getFloor());
        }
    }

}
