package org.example.securitysystem.model.entity.building;

import org.example.securitysystem.model.model_controller.builder.*;

import java.util.ArrayList;
import java.util.List;

class Building {
    private List<Floor> floors = new ArrayList<>();
    private FloorBuilder floorBuilder;
    private double floorArea;
    private int heightInFloors;

    public Building(int heightInFloors, double floorArea) {
        this.floorArea = floorArea;
        this.heightInFloors = heightInFloors;
    }

    public void buildOfficeFloor() throws Exception {
        if(floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.floorBuilder = new OfficeFloorBuilder(this.floorArea);
        floorBuilder.buildWC();
        floorBuilder.buildDiningRoom();
        floorBuilder.buildOffice();
        floorBuilder.buildHall();
        floorBuilder.buildKitchen();
        floors.add(floorBuilder.getFloor());
    }

    public void buildHostelFloor() throws Exception {
        if(floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.floorBuilder = new HostelFloorBuilder(this.floorArea);
        floorBuilder.buildWC();
        floorBuilder.buildDiningRoom();
        floorBuilder.buildLivingRoom();
        floorBuilder.buildHall();
        floorBuilder.buildKitchen();
        floors.add(floorBuilder.getFloor());
    }

    public void buildDefaultFloor() throws Exception {
        if(floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.floorBuilder = new DefaultFloorBuilder(this.floorArea);
        floorBuilder.buildWC();
        floorBuilder.buildOffice();
        floorBuilder.buildHall();
        floors.add(floorBuilder.getFloor());
    }

}
