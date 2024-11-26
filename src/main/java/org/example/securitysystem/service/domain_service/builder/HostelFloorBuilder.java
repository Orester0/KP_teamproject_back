package org.example.securitysystem.service.domain_service.builder;

import lombok.Getter;
import org.example.securitysystem.config.building_config.HostelFloorConfig;
import org.example.securitysystem.model.entity.room.*;


@Getter
public class HostelFloorBuilder extends AbstractFloorBuilder {
    public HostelFloorBuilder(double floorArea) {
        super(floorArea);
    }

    @Override
    public IFloorBuilder buildWC() {
        if (shouldBuildRoom(HostelFloorConfig.WC_RATIO, HostelFloorConfig.MIN_WC_AREA)) {
            double area = calculateRoomArea(HostelFloorConfig.WC_RATIO, HostelFloorConfig.MIN_WC_AREA);
            if (area >= HostelFloorConfig.MIN_WC_AREA) {
                addRoom(new WC(area, Math.max(2, (int)(area / HostelFloorConfig.MIN_WC_AREA))), area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildDiningRoom() {
        if (shouldBuildRoom(HostelFloorConfig.DINING_RATIO, HostelFloorConfig.MIN_DINING_AREA)) {
            double area = calculateRoomArea(HostelFloorConfig.DINING_RATIO, HostelFloorConfig.MIN_DINING_AREA);
            if (area >= HostelFloorConfig.MIN_DINING_AREA) {
                addRoom(new DiningRoom(area,
                                calculateWindowsAndDoors(area, 2, HostelFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildLivingRoom() {
        if (shouldBuildRoom(HostelFloorConfig.LIVING_RATIO, HostelFloorConfig.MIN_LIVING_AREA)) {
            double area = calculateRoomArea(HostelFloorConfig.LIVING_RATIO, HostelFloorConfig.MIN_LIVING_AREA);
            if (area >= HostelFloorConfig.MIN_LIVING_AREA) {
                addRoom(new LivingRoom(area,
                                calculateWindowsAndDoors(area, 2, HostelFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildOffice() {
        if (shouldBuildRoom(HostelFloorConfig.OFFICE_RATIO, HostelFloorConfig.MIN_OFFICE_AREA)) {
            double area = calculateRoomArea(HostelFloorConfig.OFFICE_RATIO, HostelFloorConfig.MIN_OFFICE_AREA);
            if (area >= HostelFloorConfig.MIN_OFFICE_AREA) {
                addRoom(new Office(area,
                                calculateWindowsAndDoors(area, 1, HostelFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildHall() {
        if (shouldBuildRoom(HostelFloorConfig.HALL_RATIO, HostelFloorConfig.MIN_HALL_AREA)) {
            double area = calculateRoomArea(HostelFloorConfig.HALL_RATIO, HostelFloorConfig.MIN_HALL_AREA);
            if (area >= HostelFloorConfig.MIN_HALL_AREA) {
                addRoom(new Hall(area,
                                calculateWindowsAndDoors(area, 2, HostelFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildKitchen() {
        if (shouldBuildRoom(HostelFloorConfig.KITCHEN_RATIO, HostelFloorConfig.MIN_KITCHEN_AREA)) {
            double area = calculateRoomArea(HostelFloorConfig.KITCHEN_RATIO, HostelFloorConfig.MIN_KITCHEN_AREA);
            if (area >= HostelFloorConfig.MIN_KITCHEN_AREA) {
                int doors = Math.max(2, (int)(area / HostelFloorConfig.MIN_KITCHEN_AREA));
                addRoom(new Kitchen(area,
                                calculateWindowsAndDoors(area, doors, HostelFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }
}