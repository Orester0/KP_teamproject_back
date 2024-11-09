package org.example.securitysystem.controller;

import io.swagger.annotations.Api;
import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;@RestController


@Api(tags = "Building API")
@RequestMapping("/User/{userId}/building")
public class BuildingController {

    @Autowired
    private UserService UserService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBuilding(@PathVariable Long userId, @RequestParam int heightInFloors, @RequestParam double floorArea) {
        var User = UserService.getUser(userId);
        if (User == null) {
            return "User not found.";
        }

        if (User.getBuilding() != null) {
            return "User already has a building associated.";
        }

        Building building = new Building(heightInFloors, floorArea);
        User.setBuilding(building);
        UserService.updateUser(User);
        return "Building created successfully with " + heightInFloors + " floors and floor area of " + floorArea + " sqm.";
    }

    @GetMapping("/config")
    public Building getBuildingConfig(@PathVariable Long userId) {
        var User = UserService.getUser(userId);
        Building building = getUserBuilding(User);
        return building;
    }

    @GetMapping("/floors")
    public List<Floor> getFloors(@PathVariable Long userId) {
        Building building = getUserBuilding(UserService.getUser(userId));
        return building.getFloors();
    }

    @PostMapping("/buildOfficeFloor")
    @ResponseStatus(HttpStatus.CREATED)
    public String buildOfficeFloor(@PathVariable Long userId) {
        Building building = getUserBuilding(UserService.getUser(userId));
        try {
            building.buildOfficeFloor();
            UserService.updateUser(UserService.getUser(userId));  // Save updates
            return "Office floor built successfully.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/buildHostelFloor")
    @ResponseStatus(HttpStatus.CREATED)
    public String buildHostelFloor(@PathVariable Long userId) {
        Building building = getUserBuilding(UserService.getUser(userId));
        try {
            building.buildHostelFloor();
            UserService.updateUser(UserService.getUser(userId));
            return "Hostel floor built successfully.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/setSensors")
    public String setSensors(@PathVariable Long userId) {
        Building building = getUserBuilding(UserService.getUser(userId));
        try {
            building.setSensors();
            UserService.updateUser(UserService.getUser(userId));
            return "Sensors set successfully.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("/sensors")
    public List<Sensor> getSensors(@PathVariable Long userId) {
        Building building = getUserBuilding(UserService.getUser(userId));
        return building.getFloors().stream()
                .flatMap(floor -> floor.getRooms().stream())
                .flatMap(room -> room.getSensors().stream())
                .toList();
    }

    @GetMapping("/sensor/{sensorId}")
    public Sensor getSensor(@PathVariable Long userId, @PathVariable String sensorId) {
        Building building = getUserBuilding(UserService.getUser(userId));
        return building.getFloors().stream()
                .flatMap(floor -> floor.getRooms().stream())
                .flatMap(room -> room.getSensors().stream())
                .filter(sensor -> sensor.getHashID().equals(sensorId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sensor with ID " + sensorId + " not found."));
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