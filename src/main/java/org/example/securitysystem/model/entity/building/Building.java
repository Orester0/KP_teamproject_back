package org.example.securitysystem.model.entity.building;

import lombok.Getter;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.model_controller.builder.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Building {
    private List<Floor> floors = new ArrayList<>();
    private IFloorBuilder IFloorBuilder;
    private double floorArea;
    private int heightInFloors;

    public Building(int heightInFloors, double floorArea) {
        this.floorArea = floorArea;
        this.heightInFloors = heightInFloors;
    }

    public void setSensors() throws Exception {
        if(floors.size() != heightInFloors) {
            throw new Exception("Not yet");
        }
        for(Floor floor : floors){
            for(Room room : floor.getRooms()){
                room.calculateSensor();
            }
        }
    }

    public void buildOfficeFloor() throws Exception {
        if(floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.IFloorBuilder = new OfficeFloorBuilder(this.floorArea);
        Floor officeFloor = this.IFloorBuilder
                .buildOffice()
                .buildHall()
                .buildDiningRoom()
                .buildKitchen()
                .buildWC()
                .getFloor();
        floors.add(officeFloor);
    }

    public void buildHostelFloor() throws Exception {
        if(floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.IFloorBuilder = new HostelFloorBuilder(this.floorArea);
        Floor hostelFloor = this.IFloorBuilder
                .buildLivingRoom()
                .buildDiningRoom()
                .buildKitchen()
                .buildHall()
                .buildWC()
                .getFloor();
        floors.add(hostelFloor);
    }

    public void buildDefaultFloor() throws Exception {
        if(floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.IFloorBuilder = new DefaultFloorBuilder(this.floorArea);
        Floor defaultFloor = this.IFloorBuilder
                .buildLivingRoom()
                .buildDiningRoom()
                .buildKitchen()
                .buildHall()
                .buildOffice()
                .buildWC()
                .getFloor();
        floors.add(defaultFloor);
    }

}
