package org.example.securitysystem.model.model_controller.command;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;

public class DefaultFloorCommand implements FloorCommand {
    @Override
    public void execute(Building building) throws BuildingException {
        building.buildDefaultFloor();
    }
}
