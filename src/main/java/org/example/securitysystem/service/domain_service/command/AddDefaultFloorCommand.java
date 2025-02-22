package org.example.securitysystem.service.domain_service.command;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;

public class AddDefaultFloorCommand implements IAddFloorCommand {
    @Override
    public void execute(Building building) throws BuildingException {
        building.buildDefaultFloor();
    }
}
