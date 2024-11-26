package org.example.securitysystem.service;

import org.example.securitysystem.model.model_controller.command.DefaultFloorCommand;
import org.example.securitysystem.model.model_controller.command.FloorCommand;
import org.example.securitysystem.model.model_controller.command.HostelFloorCommand;
import org.example.securitysystem.model.model_controller.command.OfficeFloorCommand;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FloorCommandRegistry {
    private final Map<String, FloorCommand> commands = new HashMap<>();

    public FloorCommandRegistry() {
        commands.put("default", new DefaultFloorCommand());
        commands.put("office", new OfficeFloorCommand());
        commands.put("hostel", new HostelFloorCommand());
    }

    public FloorCommand getCommand(String floorType) {
        return commands.get(floorType.toLowerCase());
    }
}
