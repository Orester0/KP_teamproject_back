package org.example.securitysystem.model.model_controller.builder;

import lombok.Getter;
import org.example.securitysystem.config.OfficeFloorConfig;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.*;

@Getter
public class OfficeFloorBuilder  extends AbstractFloorBuilder {
    public OfficeFloorBuilder(double floorArea) {
        super(floorArea);
    }

    @Override
    public IFloorBuilder buildWC() {
        if (shouldBuildRoom(OfficeFloorConfig.WC_RATIO)) {
            double area = calculateRoomArea(OfficeFloorConfig.WC_RATIO);
            addRoom(new WC(area, 1), area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildDiningRoom() {
        if (shouldBuildRoom(OfficeFloorConfig.DINING_RATIO)) {
            double area = calculateRoomArea(OfficeFloorConfig.DINING_RATIO);
            addRoom(new DiningRoom(area,
                            calculateWindowsAndDoors(area, 1, OfficeFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildLivingRoom() {
        // Note: Office buildings typically don't have living rooms (LIVING_RATIO = 0)
        if (shouldBuildRoom(OfficeFloorConfig.LIVING_RATIO)) {
            double area = calculateRoomArea(OfficeFloorConfig.LIVING_RATIO);
            addRoom(new LivingRoom(area,
                            calculateWindowsAndDoors(area, 2, OfficeFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildOffice() {
        if (shouldBuildRoom(OfficeFloorConfig.OFFICE_RATIO)) {
            double area = calculateRoomArea(OfficeFloorConfig.OFFICE_RATIO);
            addRoom(new Office(area,
                            calculateWindowsAndDoors(area, 1, OfficeFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildHall() {
        if (shouldBuildRoom(OfficeFloorConfig.HALL_RATIO)) {
            double area = calculateRoomArea(OfficeFloorConfig.HALL_RATIO);
            addRoom(new Hall(area,
                            calculateWindowsAndDoors(area, 2, OfficeFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildKitchen() {
        if (shouldBuildRoom(OfficeFloorConfig.KITCHEN_RATIO)) {
            double area = calculateRoomArea(OfficeFloorConfig.KITCHEN_RATIO);
            addRoom(new Kitchen(area,
                            calculateWindowsAndDoors(area, 1, OfficeFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }
}
