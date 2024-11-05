package org.example.securitysystem.model.model_controller.builder;

import lombok.Getter;
import org.example.securitysystem.config.DefaultFloorConfig;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.*;

@Getter
public class DefaultFloorBuilder extends  AbstractFloorBuilder {
    public DefaultFloorBuilder(double floorArea) {
        super(floorArea);
    }

    @Override
    public IFloorBuilder buildWC() {
        if (shouldBuildRoom(DefaultFloorConfig.WC_RATIO)) {
            double area = calculateRoomArea(DefaultFloorConfig.WC_RATIO);
            addRoom(new WC(area, 1), area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildDiningRoom() {
        if (shouldBuildRoom(DefaultFloorConfig.DINING_RATIO)) {
            double area = calculateRoomArea(DefaultFloorConfig.DINING_RATIO);
            addRoom(new DiningRoom(area,
                            calculateWindowsAndDoors(area, 1, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildLivingRoom() {
        if (shouldBuildRoom(DefaultFloorConfig.LIVING_RATIO)) {
            double area = calculateRoomArea(DefaultFloorConfig.LIVING_RATIO);
            addRoom(new LivingRoom(area,
                            calculateWindowsAndDoors(area, 2, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildOffice() {
        if (shouldBuildRoom(DefaultFloorConfig.OFFICE_RATIO)) {
            double area = calculateRoomArea(DefaultFloorConfig.OFFICE_RATIO);
            addRoom(new Office(area,
                            calculateWindowsAndDoors(area, 1, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildHall() {
        if (shouldBuildRoom(DefaultFloorConfig.HALL_RATIO)) {
            double area = calculateRoomArea(DefaultFloorConfig.HALL_RATIO);
            addRoom(new Hall(area,
                            calculateWindowsAndDoors(area, 2, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildKitchen() {
        if (shouldBuildRoom(DefaultFloorConfig.KITCHEN_RATIO)) {
            double area = calculateRoomArea(DefaultFloorConfig.KITCHEN_RATIO);
            addRoom(new Kitchen(area,
                            calculateWindowsAndDoors(area, 1, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }
}

