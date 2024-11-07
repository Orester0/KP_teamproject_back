package org.example.securitysystem.model.entity.building;

import lombok.Getter;
import org.example.securitysystem.model.entity.room.*;
import org.example.securitysystem.model.entity.security_system.sensors.*;
import org.example.securitysystem.model.model_controller.builder.*;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


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

    public void buildOfficeFloor() throws Exception {
        if (floors.size() == heightInFloors) {
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
        if (floors.size() == heightInFloors) {
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
        if (floors.size() == heightInFloors) {
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
