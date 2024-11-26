package org.example.securitysystem.service.domain_service.builder;

import lombok.Getter;
import org.example.securitysystem.config.building_config.DefaultFloorConfig;
import org.example.securitysystem.model.entity.room.*;

@Getter
public class DefaultFloorBuilder extends AbstractFloorBuilder {
    public DefaultFloorBuilder(double floorArea) {
        super(floorArea);
    }

    @Override
    public IFloorBuilder buildWC() {
        if (shouldBuildRoom(DefaultFloorConfig.WC_RATIO, DefaultFloorConfig.MIN_WC_AREA)) {
            double area = calculateRoomArea(DefaultFloorConfig.WC_RATIO, DefaultFloorConfig.MIN_WC_AREA);
            addRoom(new WC(area, 1), area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildDiningRoom() {
        if (shouldBuildRoom(DefaultFloorConfig.DINING_RATIO, DefaultFloorConfig.MIN_DINING_AREA)) {
            double area = calculateRoomArea(DefaultFloorConfig.DINING_RATIO, DefaultFloorConfig.MIN_DINING_AREA);
            addRoom(new DiningRoom(area,
                            calculateWindowsAndDoors(area, 1, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildLivingRoom() {
        if (shouldBuildRoom(DefaultFloorConfig.LIVING_RATIO, DefaultFloorConfig.MIN_LIVING_AREA)) {
            double area = calculateRoomArea(DefaultFloorConfig.LIVING_RATIO, DefaultFloorConfig.MIN_LIVING_AREA);
            addRoom(new LivingRoom(area,
                            calculateWindowsAndDoors(area, 2, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildOffice() {
        if (shouldBuildRoom(DefaultFloorConfig.OFFICE_RATIO, DefaultFloorConfig.MIN_OFFICE_AREA)) {
            double area = calculateRoomArea(DefaultFloorConfig.OFFICE_RATIO, DefaultFloorConfig.MIN_OFFICE_AREA);
            addRoom(new Office(area,
                            calculateWindowsAndDoors(area, 1, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildHall() {
        if (shouldBuildRoom(DefaultFloorConfig.HALL_RATIO, DefaultFloorConfig.MIN_HALL_AREA)) {
            double area = calculateRoomArea(DefaultFloorConfig.HALL_RATIO, DefaultFloorConfig.MIN_HALL_AREA);
            addRoom(new Hall(area,
                            calculateWindowsAndDoors(area, 2, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildKitchen() {
        if (shouldBuildRoom(DefaultFloorConfig.KITCHEN_RATIO, DefaultFloorConfig.MIN_KITCHEN_AREA)) {
            double area = calculateRoomArea(DefaultFloorConfig.KITCHEN_RATIO, DefaultFloorConfig.MIN_KITCHEN_AREA);
            addRoom(new Kitchen(area,
                            calculateWindowsAndDoors(area, 1, DefaultFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }
}
