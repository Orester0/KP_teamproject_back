package org.example.securitysystem.service.domain_service.command;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;

public interface IAddFloorCommand {
    void execute(Building building) throws BuildingException;
}
