package org.example.securitysystem.model.model_controller.command;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;

public interface FloorCommand {
    void execute(Building building) throws BuildingException;
}
