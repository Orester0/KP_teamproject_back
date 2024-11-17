package org.example.securitysystem.controller;
import org.example.securitysystem.model.dto.BuildingRequest;
import org.example.securitysystem.model.dto.SimulationResponse;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/building")
public class BuildingController {

    private final SessionService sessionService;

    @Autowired
    public BuildingController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBuilding(@RequestBody BuildingRequest request) {
        try {
            var session = sessionService.getSession(request.getSessionId());
            if (session == null) {
                return ResponseEntity.badRequest().body("Session not found.");
            }
            if (session.getBuilding() != null) {
                return ResponseEntity.badRequest().body("Session already has a building associated.");
            }

            Building building = new Building(request.getHeightInFloors(), request.getFloorArea());
            session.setBuilding(building);
            sessionService.updateSession(session);
            return ResponseEntity.ok("Building created successfully with " + request.getHeightInFloors() +
                    " floors and floor area of " + request.getFloorArea() + " sqm.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/addDefaultFloor")
    public ResponseEntity<String> addDefaultFloor(@RequestBody BuildingRequest request) {
        try {
            var session = sessionService.getSession(request.getSessionId());
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.buildDefaultFloor();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Default floor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/addOfficeFloor")
    public ResponseEntity<String> addOfficeFloor(@RequestBody BuildingRequest request) {
        try {
            var session = sessionService.getSession(request.getSessionId());
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.buildOfficeFloor();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Office floor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/addHostelFloor")
    public ResponseEntity<String> addHostelFloor(@RequestBody BuildingRequest request) {
        try {
            var session = sessionService.getSession(request.getSessionId());
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.buildHostelFloor();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Hostel floor added successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeBuilding(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Building is already finalized.");
            }
            building.finalizeBuilding();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Building finalized successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    @PostMapping("/startSimulation")
    public ResponseEntity<SimulationResponse> startSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            if (!building.isFinalized()) {
                return ResponseEntity.badRequest()
                        .body(new SimulationResponse(null, "Building must be finalized before starting simulation"));
            }

            String socketTopic = "asd";//simulationService.startSimulation(userId, building);

            return ResponseEntity.ok(new SimulationResponse(socketTopic, "Simulation started successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/config")
    public ResponseEntity<Building> getBuildingConfig(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (!building.isFinalized()) {
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(building);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/removeFloor")
    public ResponseEntity<String> removeFloor(@RequestParam Long sessionId, @RequestParam int floorId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.badRequest().body("Cannot modify finalized building.");
            }
            building.removeFloor(floorId);
            sessionService.updateSession(session);
            return ResponseEntity.ok("Floor removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(handleError(e));
        }
    }

    private String handleError(Exception e) {
        return "Error occurred: " + e.getMessage();
    }

    private Building getBuildingFromSession(Session session) {
        if (session == null) {
            throw new RuntimeException("Session not found.");
        }
        if (session.getBuilding() == null) {
            throw new RuntimeException("Session does not have a building associated.");
        }
        return session.getBuilding();
    }
}