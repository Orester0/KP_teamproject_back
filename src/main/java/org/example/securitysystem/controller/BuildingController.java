package org.example.securitysystem.controller;
import org.example.securitysystem.model.dto.BuildingRequest;
import org.example.securitysystem.model.entity.User;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/building")
public class BuildingController {

    private final UserService userService;

    @Autowired
    public BuildingController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBuilding(@RequestBody BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found.");
            }
            if (user.getBuilding() != null) {
                return ResponseEntity.badRequest().body("User already has a building associated.");
            }

            Building building = new Building(request.getHeightInFloors(), request.getFloorArea());
            user.setBuilding(building);
            userService.updateUser(user);
            return ResponseEntity.ok("Building created successfully with " + request.getHeightInFloors() +
                    " floors and floor area of " + request.getFloorArea() + " sqm.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/addDefaultFloor")
    public ResponseEntity<String> addDefaultFloor(@RequestBody BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            Building building = getUserBuilding(user);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.buildDefaultFloor();
            userService.updateUser(user);
            return ResponseEntity.ok("Default floor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/addOfficeFloor")
    public ResponseEntity<String> addOfficeFloor(@RequestBody BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            Building building = getUserBuilding(user);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.buildOfficeFloor();
            userService.updateUser(user);
            return ResponseEntity.ok("Office floor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/addHostelFloor")
    public ResponseEntity<String> addHostelFloor(@RequestBody BuildingRequest request) {
        try {
            var user = userService.getUser(request.getUserId());
            Building building = getUserBuilding(user);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.buildHostelFloor();
            userService.updateUser(user);
            return ResponseEntity.ok("Hostel floor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeBuilding(@RequestParam Long userId) {
        try {
            var user = userService.getUser(userId);
            Building building = getUserBuilding(user);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Building is already finalized.");
            }
            building.finalizeBuilding();
            userService.updateUser(user);
            return ResponseEntity.ok("Building finalized successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @GetMapping("/config")
    public ResponseEntity<Building> getBuildingConfig(@RequestParam Long userId) {
        try {
            var user = userService.getUser(userId);
            Building building = getUserBuilding(user);
            if (!building.isFinalized()) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(building);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/removeFloor")
    public ResponseEntity<String> removeFloor(@RequestParam Long userId, @RequestParam int floorId) {
        try {
            var user = userService.getUser(userId);
            Building building = getUserBuilding(user);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.removeFloor(floorId);
            userService.updateUser(user);
            return ResponseEntity.ok("Floor removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
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