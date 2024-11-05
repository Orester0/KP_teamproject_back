package org.example.securitysystem.model.model_controller.builder;

import lombok.Getter;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.*;
import org.example.securitysystem.config.*;


@Getter
public class HostelFloorBuilder extends AbstractFloorBuilder {
    public HostelFloorBuilder(double floorArea) {
        super(floorArea);
    }

    @Override
    public IFloorBuilder buildWC() {
        if (shouldBuildRoom(HostelFloorConfig.WC_RATIO)) {
            double area = calculateRoomArea(HostelFloorConfig.WC_RATIO);
            addRoom(new WC(area, 1), area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildDiningRoom() {
        if (shouldBuildRoom(HostelFloorConfig.DINING_RATIO)) {
            double area = calculateRoomArea(HostelFloorConfig.DINING_RATIO);
            addRoom(new DiningRoom(area,
                            calculateWindowsAndDoors(area, 1, HostelFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildLivingRoom() {
        if (shouldBuildRoom(HostelFloorConfig.LIVING_RATIO)) {
            double area = calculateRoomArea(HostelFloorConfig.LIVING_RATIO);
            addRoom(new LivingRoom(area,
                            calculateWindowsAndDoors(area, 2, HostelFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildOffice() {
        if (shouldBuildRoom(HostelFloorConfig.OFFICE_RATIO)) {
            double area = calculateRoomArea(HostelFloorConfig.OFFICE_RATIO);
            addRoom(new Office(area,
                            calculateWindowsAndDoors(area, 1, HostelFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildHall() {
        if (shouldBuildRoom(HostelFloorConfig.HALL_RATIO)) {
            double area = calculateRoomArea(HostelFloorConfig.HALL_RATIO);
            addRoom(new Hall(area,
                            calculateWindowsAndDoors(area, 2, HostelFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }

    @Override
    public IFloorBuilder buildKitchen() {
        if (shouldBuildRoom(HostelFloorConfig.KITCHEN_RATIO)) {
            double area = calculateRoomArea(HostelFloorConfig.KITCHEN_RATIO);
            addRoom(new Kitchen(area,
                            calculateWindowsAndDoors(area, 1, HostelFloorConfig.AREA_PER_WINDOW)),
                    area);
        }
        return this;
    }
}
