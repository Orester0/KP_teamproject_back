package org.example.securitysystem.controller;
import org.example.securitysystem.model.dto.BuildingRequest;
import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class BuildingController {

    private final UserService userService;

    @Autowired
    public BuildingController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/building/create")
    @SendTo("/topic/building")
    public String createBuilding(BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            if (user == null) {
                throw new IllegalArgumentException("User not found.");
            }
            if (user.getBuilding() != null) {
                throw new IllegalStateException("User already has a building associated.");
            }

            Building building = new Building(request.getHeightInFloors(), request.getFloorArea());
            user.setBuilding(building);
            userService.updateUser(user);
            return "Building created successfully with " + request.getHeightInFloors() + " floors and floor area of " + request.getFloorArea() + " sqm.";
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @MessageMapping("/building/addDefaultFloor")
    @SendTo("/topic/building")
    public String addDefaultFloor(BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            Building building = getUserBuilding(user);
            building.buildDefaultFloor();
            userService.updateUser(user);
            return "Default floor added successfully.";
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @MessageMapping("/building/addOfficeFloor")
    @SendTo("/topic/building")
    public String addOfficeFloor(BuildingRequest request){
        try {
            var user = userService.getUser(request.getUserId());
            Building building = getUserBuilding(user);
            building.buildOfficeFloor();
            userService.updateUser(user);
            return "Office floor added successfully.";
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @MessageMapping("/building/addHostelFloor")
    @SendTo("/topic/building")
    public String addHostelFloor(BuildingRequest request){
        try {
            var user = userService.getUser(request.getUserId());
            Building building = getUserBuilding(user);
            building.buildHostelFloor();
            userService.updateUser(user);
            return "Hostel floor added successfully.";
        } catch (Exception e) {
            return handleError(e);
        }
    }

    @MessageMapping("/building/config")
    @SendTo("/topic/buildingConfig")
    public Building getBuildingConfig(BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            return getUserBuilding(user);
        } catch (Exception e) {
            throw new RuntimeException("Building configuration could not be retrieved: " + e.getMessage());
        }
    }

    private String handleError(Exception e) {
        return "Error occurred: " + e.getMessage();
    }

    private Building getUserBuilding(User user) {
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        if (user.getBuilding() == null) {
            throw new RuntimeException("User does not have a building associated.");
        }
        return user.getBuilding();
    }
}
