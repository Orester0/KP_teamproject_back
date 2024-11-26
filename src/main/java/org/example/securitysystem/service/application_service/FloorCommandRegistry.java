package org.example.securitysystem.service.application_service;

import org.example.securitysystem.service.domain_service.command.AddDefaultFloorCommand;
import org.example.securitysystem.service.domain_service.command.IAddFloorCommand;
import org.example.securitysystem.service.domain_service.command.AddHostelFloorCommand;
import org.example.securitysystem.service.domain_service.command.AddOfficeFloorCommand;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FloorCommandRegistry {
    private final Map<String, IAddFloorCommand> commands = new HashMap<>();

    public FloorCommandRegistry() {
        commands.put("default", new AddDefaultFloorCommand());
        commands.put("office", new AddOfficeFloorCommand());
        commands.put("hostel", new AddHostelFloorCommand());
    }

    public IAddFloorCommand getCommand(String floorType) {
        return commands.get(floorType.toLowerCase());
    }
}
