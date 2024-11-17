package org.example.securitysystem.model.entity.building;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import org.example.securitysystem.model.entity.room.*;
import org.example.securitysystem.model.entity.security_system.sensors.*;
import org.example.securitysystem.model.model_controller.builder.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private transient IFloorBuilder IFloorBuilder;

    static Pattern shortenerPattern = Pattern.compile("^([a-zA-Z]{2}).*");

    public Building(int heightInFloors, double floorArea) {
        this.floorArea = floorArea;
        this.heightInFloors = heightInFloors;
    }

    public static String shortenName(String name) {
        Matcher matcher = shortenerPattern.matcher(name);
        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }

    private void setSensors() throws Exception {
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
                floor.setID(floorAndRoomBuilder.toString());

                floorAndRoomBuilder.append(String.format("/%02d", roomNumber));
                room.setID(floorAndRoomBuilder.toString());

                int sensorCount = 0;
                for (Sensor sensor : room.getSensors()) {
                    StringBuilder sensorIdBuilder = new StringBuilder(floorAndRoomBuilder);
                    sensorIdBuilder.append(String.format("/%02d", ++sensorCount));
                    sensor.setID(sensorIdBuilder.toString());
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
        IFloorBuilder = null;
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