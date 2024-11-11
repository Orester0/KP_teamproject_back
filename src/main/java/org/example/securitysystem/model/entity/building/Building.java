package org.example.securitysystem.model.entity.building;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import org.example.securitysystem.model.entity.room.*;
import org.example.securitysystem.model.entity.security_system.sensors.*;
import org.example.securitysystem.model.model_controller.builder.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Building implements Serializable {
    @Expose
    private List<Floor> floors = new ArrayList<>();
    @Expose
    private double floorArea;
    @Expose
    private int heightInFloors;
    @Expose
    private boolean isFinalized = false;

    private IFloorBuilder IFloorBuilder;

    public Building(int heightInFloors, double floorArea) {
        this.floorArea = floorArea;
        this.heightInFloors = heightInFloors;
    }

    public void setSensors() throws Exception {
        validateNotFinalized();
        if (floors.size() != heightInFloors) {
            throw new Exception("Number of floors does not match the expected height");
        }

        int floorNumber = 1;
        int roomNumber = 1;
        for (Floor floor : floors) {
            for (Room room : floor.getRooms()) {
                room.calculateSensor();

                StringBuilder floorAndRoomBuilder = new StringBuilder();
                floorAndRoomBuilder.append(String.format("%02d", floorNumber));

                String name1 = room.getClass().getSimpleName();
                floorAndRoomBuilder.append("_").append(name1).append("_");

                floorAndRoomBuilder.append(String.format("%03d", roomNumber));

                int sensorCount = 0;
                for (Sensor sensor : room.getSensors()) {
                    StringBuilder sensorIdBuilder = new StringBuilder(floorAndRoomBuilder);

                    String name2 = sensor.getClass().getSimpleName();
                    sensorIdBuilder.append("_").append(name2).append("_");

                    sensorIdBuilder.append(String.format("%03d", ++sensorCount));
                    sensor.setHashID(sensorIdBuilder.toString());
                    sensorIdBuilder.setLength(floorAndRoomBuilder.length() - 3);
                }
                roomNumber++;
            }
            floorNumber++;
            roomNumber = 1;
        }
    }

    public void removeFloor(int id) throws Exception {
        validateNotFinalized();
        if (id < 0 || id >= floors.size()) {
            throw new Exception("Invalid ID");
        }
        try {
            floors.remove(id);
        } catch (Exception e) {
            throw new Exception("Can't remove floor");
        }
    }

    public void buildOfficeFloor() throws Exception {
        validateNotFinalized();
        if (floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.IFloorBuilder = new OfficeFloorBuilder(this.floorArea);
        this.IFloorBuilder
                .buildOffice()
                .buildDiningRoom()
                .buildKitchen()
                .buildWC()
                .buildHall();

        Floor officeFloor = this.IFloorBuilder.finalizeFloor();
        floors.add(officeFloor);
    }

    public void buildHostelFloor() throws Exception {
        validateNotFinalized();
        if (floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.IFloorBuilder = new HostelFloorBuilder(this.floorArea);
        this.IFloorBuilder
                .buildLivingRoom()
                .buildDiningRoom()
                .buildKitchen()
                .buildWC()
                .buildHall();

        Floor hostelFloor = this.IFloorBuilder.finalizeFloor();
        floors.add(hostelFloor);
    }

    public void buildDefaultFloor() throws Exception {
        validateNotFinalized();
        if (floors.size() == heightInFloors) {
            throw new Exception("Already has all floors");
        }
        this.IFloorBuilder = new DefaultFloorBuilder(this.floorArea);
        this.IFloorBuilder
                .buildLivingRoom()
                .buildDiningRoom()
                .buildKitchen()
                .buildOffice()
                .buildWC()
                .buildHall();

        Floor defaultFloor = this.IFloorBuilder.finalizeFloor();
        floors.add(defaultFloor);
    }

    public void finalizeBuilding() throws Exception {
        if (floors.size() != heightInFloors) {
            throw new Exception("Cannot finalize: Number of floors does not match the expected height");
        }
        setSensors();
        isFinalized = true;
    }

    private void validateNotFinalized() throws Exception {
        if (isFinalized) {
            throw new Exception("Building is finalized and cannot be modified");
        }
    }

    public boolean isFinalized() {
        return isFinalized;
    }
}