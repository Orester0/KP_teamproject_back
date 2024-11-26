package org.example.securitysystem.service.domain_service.builder;

import lombok.Getter;
import org.example.securitysystem.config.building_config.OfficeFloorConfig;
import org.example.securitysystem.model.entity.room.*;
@Getter
public class OfficeFloorBuilder extends AbstractFloorBuilder {
    public OfficeFloorBuilder(double floorArea) {
        super(floorArea);
    }

    @Override
    public IFloorBuilder buildWC() {
        if (shouldBuildRoom(OfficeFloorConfig.WC_RATIO, OfficeFloorConfig.MIN_WC_AREA)) {
            double area = calculateRoomArea(OfficeFloorConfig.WC_RATIO, OfficeFloorConfig.MIN_WC_AREA);
            if (area >= OfficeFloorConfig.MIN_WC_AREA) {
                int wcUnits = Math.max(2, (int)(area / OfficeFloorConfig.MIN_WC_AREA));
                addRoom(new WC(area, wcUnits), area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildDiningRoom() {
        if (shouldBuildRoom(OfficeFloorConfig.DINING_RATIO, OfficeFloorConfig.MIN_DINING_AREA)) {
            double area = calculateRoomArea(OfficeFloorConfig.DINING_RATIO, OfficeFloorConfig.MIN_DINING_AREA);
            if (area >= OfficeFloorConfig.MIN_DINING_AREA) {
                int doors = Math.max(2, (int)(area / 50));
                addRoom(new DiningRoom(area,
                                calculateWindowsAndDoors(area, doors, OfficeFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildLivingRoom() {
        return this;
    }

    @Override
    public IFloorBuilder buildOffice() {
        if (shouldBuildRoom(OfficeFloorConfig.OFFICE_RATIO, OfficeFloorConfig.MIN_OFFICE_AREA)) {
            double area = calculateRoomArea(OfficeFloorConfig.OFFICE_RATIO, OfficeFloorConfig.MIN_OFFICE_AREA);
            if (area >= OfficeFloorConfig.MIN_OFFICE_AREA) {
                int officeCount = Math.max(1, (int)(area / OfficeFloorConfig.MIN_OFFICE_AREA));
                double individualOfficeArea = area / officeCount;

                for (int i = 0; i < officeCount; i++) {
                    addRoom(new Office(individualOfficeArea,
                                    calculateWindowsAndDoors(individualOfficeArea, 1, OfficeFloorConfig.AREA_PER_WINDOW)),
                            individualOfficeArea);
                }
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildHall() {
        if (shouldBuildRoom(OfficeFloorConfig.HALL_RATIO, OfficeFloorConfig.MIN_HALL_AREA)) {
            double area = calculateRoomArea(OfficeFloorConfig.HALL_RATIO, OfficeFloorConfig.MIN_HALL_AREA);
            if (area >= OfficeFloorConfig.MIN_HALL_AREA) {
                int doors = Math.max(2, (int)(area / 30));
                addRoom(new Hall(area,
                                calculateWindowsAndDoors(area, doors, OfficeFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }

    @Override
    public IFloorBuilder buildKitchen() {
        if (shouldBuildRoom(OfficeFloorConfig.KITCHEN_RATIO, OfficeFloorConfig.MIN_KITCHEN_AREA)) {
            double area = calculateRoomArea(OfficeFloorConfig.KITCHEN_RATIO, OfficeFloorConfig.MIN_KITCHEN_AREA);
            if (area >= OfficeFloorConfig.MIN_KITCHEN_AREA) {
                addRoom(new Kitchen(area,
                                calculateWindowsAndDoors(area, 2, OfficeFloorConfig.AREA_PER_WINDOW)),
                        area);
            }
        }
        return this;
    }
}