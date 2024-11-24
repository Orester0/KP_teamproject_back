package org.example.securitysystem.model.entity.building;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import org.example.securitysystem.exception.BuildingException;
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

    public Building(int heightInFloors, double floorArea)
    {
        this.floorArea = floorArea;
        this.heightInFloors = heightInFloors;
    }

    public void finalizeBuilding() throws BuildingException
    {
        if (floors.size() != heightInFloors)
        {
            throw new BuildingException("Cannot finalize: Number of floors does not match the expected height");
        }
        setSensors();
        isFinalized = true;
        IFloorBuilder = null;
    }

    private void setSensors() throws BuildingException
    {
        validateNotFinalized();
        if (floors.size() != heightInFloors)
        {
            throw new BuildingException("Number of floors does not match the expected height");
        }

        int sensorNumber = 1;
        int roomNumber = 1;
        int floorNumber = 1;

        for (Floor floor : floors)
        {
            floor.setFloorNumber(floorNumber++);
            for (Room room : floor.getRooms())
            {
                room.setID(String.valueOf(roomNumber++));
                room.calculateSensor();
                for(Sensor sensor : room.getSensors())
                {
                    sensor.setID(String.valueOf(sensorNumber++));
                }
            }
        }
    }

    public void removeFloor(int floorNumber) throws BuildingException
    {
        validateNotFinalized();
        if (floorNumber < 0 || floorNumber >= floors.size())
        {
            throw new BuildingException("Invalid number of floor");
        }
        try
        {
            floors.remove(floorNumber);
        }
        catch (Exception e)
        {
            throw new BuildingException("Can't remove floor");
        }
    }

    public void buildOfficeFloor() throws BuildingException {
        validateNotFinalized();
        if (floors.size() == heightInFloors) {
            throw new BuildingException("Already has all floors");
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

    public void buildHostelFloor() throws BuildingException {
        validateNotFinalized();
        if (floors.size() == heightInFloors) {
            throw new BuildingException("Already has all floors");
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

    public void buildDefaultFloor() throws BuildingException {
        validateNotFinalized();
        if (floors.size() == heightInFloors) {
            throw new BuildingException("Already has all floors");
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


    private void validateNotFinalized() throws BuildingException {
        if (isFinalized) {
            throw new BuildingException("Building is finalized and cannot be modified");
        }
    }

    public boolean isFinalized() {
        return isFinalized;
    }
}