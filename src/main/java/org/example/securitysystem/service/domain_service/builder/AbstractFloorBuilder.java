package org.example.securitysystem.service.domain_service.builder;

import lombok.Getter;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;

@Getter
public abstract class AbstractFloorBuilder implements IFloorBuilder {
    protected final double floorArea;
    protected final Floor floor;
    protected double remainingArea;

    protected AbstractFloorBuilder(double floorArea) {
        if (floorArea <= 0) {
            throw new IllegalArgumentException("Floor area must be positive");
        }
        this.floorArea = floorArea;
        this.remainingArea = floorArea;
        this.floor = new Floor();
    }

    protected int calculateWindowsAndDoors(double area, int doors, double areaPerWindow) {
        if (area <= 0 || doors < 0 || areaPerWindow <= 0) {
            throw new IllegalArgumentException("Invalid parameters for window and door calculation");
        }
        double windowsExact = area / areaPerWindow;
        int windows = Math.max(1, (int) Math.ceil(windowsExact));
        return windows + doors;
    }

    protected double calculateRoomArea(double ratio, double minArea) {
        if (ratio <= 0 || ratio > 1 || minArea <= 0) {
            throw new IllegalArgumentException("Invalid ratio or minimum area");
        }

        double targetArea = floorArea * ratio;
        double adjustedArea = Math.max(targetArea, minArea);

        if (adjustedArea > remainingArea) {
            return remainingArea >= minArea ? remainingArea : 0;
        }

        return adjustedArea;
    }

    protected boolean shouldBuildRoom(double ratio, double minArea) {
        if (ratio <= 0 || ratio > 1 || minArea <= 0) {
            return false;
        }

        double requiredArea = Math.max(floorArea * ratio, minArea);
        return remainingArea >= minArea && remainingArea >= requiredArea * 0.5;
    }

    protected void addRoom(Room room, double area) {
        if (room == null || area <= 0 || area > remainingArea) {
            throw new IllegalArgumentException("Invalid room or area parameters");
        }
        floor.addRoom(room);
        remainingArea -= area;
    }

    public Floor finalizeFloor() {
        if (remainingArea > 0 && !floor.getRooms().isEmpty()) {
            Room lastRoom = floor.getRooms().get(floor.getRooms().size() - 1);
            if (lastRoom != null) {
                double newArea = lastRoom.getArea() + remainingArea;
                lastRoom.setArea(newArea);
                remainingArea = 0;
            }
        }
        return floor;
    }
}