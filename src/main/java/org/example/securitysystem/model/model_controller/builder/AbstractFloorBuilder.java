package org.example.securitysystem.model.model_controller.builder;

import lombok.Getter;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;

@Getter
abstract class AbstractFloorBuilder implements IFloorBuilder {
    protected final double floorArea;
    protected final Floor floor;
    protected double remainingArea;

    protected AbstractFloorBuilder(double floorArea) {
        this.floorArea = floorArea;
        this.remainingArea = floorArea;
        this.floor = new Floor();
    }

    protected int calculateWindowsAndDoors(double area, int doors, double areaPerWindow) {
        int windows = Math.max(1, (int)(area / areaPerWindow));
        return windows + doors;
    }

    protected double calculateRoomArea(double ratio) {
        double idealArea = floorArea * ratio;
        return Math.min(idealArea, remainingArea);
    }

    protected boolean shouldBuildRoom(double ratio) {
        return remainingArea >= calculateRoomArea(ratio);
    }

    protected void addRoom(Room room, double area) {
        floor.addRoom(room);
        remainingArea -= area;
    }
}
