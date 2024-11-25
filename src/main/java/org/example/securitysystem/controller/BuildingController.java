package org.example.securitysystem.controller;
import com.google.gson.Gson;
import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.dto.BuildingRequest;
import org.example.securitysystem.model.dto.SimulationResponse;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.SessionService;
import org.example.securitysystem.service.SimulationService;
import org.example.securitysystem.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/building")
public class BuildingController {

    private final Gson gson;
    private final SessionService sessionService;
    private final WebSocketService webSocketService;
    private final SimulationService simulationService;

    @Autowired
    public BuildingController(SessionService sessionService, WebSocketService webSocketService, SimulationService simulationService) {
        this.sessionService = sessionService;
        this.webSocketService = webSocketService;
        this.simulationService = simulationService;
        this.gson = new Gson();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBuilding(@RequestBody BuildingRequest request) {
        try {
            var session = sessionService.getSession(request.getSessionId());
            if (session == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session not found");
            }
            if (session.getBuilding() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session already has a building associated.");
            }

            Building building = new Building(request.getHeightInFloors(), request.getFloorArea());
            session.setBuilding(building);
            sessionService.updateSession(session);
            return ResponseEntity.ok("Building created successfully with " + request.getHeightInFloors() +
                    " floors and floor area of " + request.getFloorArea() + " sqm.");
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleError(e));
        }
    }

    @PostMapping("/addDefaultFloor")
    public ResponseEntity<String> addDefaultFloor(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot modify finalized building.");
            }
            building.buildDefaultFloor();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Default floor added successfully.");
        }
        catch (BuildingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handleError(e));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleError(e));
        }
    }

    @PostMapping("/addOfficeFloor")
    public ResponseEntity<String> addOfficeFloor(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot modify finalized building.");
            }
            building.buildOfficeFloor();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Office floor added successfully.");
        } catch (BuildingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handleError(e));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleError(e));
        }
    }

    @PostMapping("/addHostelFloor")
    public ResponseEntity<String> addHostelFloor(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot modify finalized building.");
            }
            building.buildHostelFloor();
            sessionService.updateSession(session);
            return ResponseEntity.ok("Hostel floor added successfully.");
        } catch (BuildingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handleError(e));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleError(e));
        }
    }

    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeBuilding(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Building is already finalized.");
            }
            building.finalizeBuilding();
            sessionService.updateSession(session);


            // get full building from database with IDs
            // from some service

            return ResponseEntity.ok("Building finalized successfully.");
        } catch (BuildingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handleError(e));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleError(e));
        }
    }

    @PostMapping("/startSimulation")
    public ResponseEntity<SimulationResponse> startSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            if (!building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimulationResponse(null, "Building must be finalized before starting simulation"));
            }

            String socketTopic = webSocketService.createTopicForSession(sessionId);
            simulationService.startSimulation(sessionId, building, socketTopic);

            return ResponseEntity.ok(new SimulationResponse(socketTopic, "Simulation started successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }

    }


    @PostMapping("/stopSimulation")
    public ResponseEntity<SimulationResponse> stopSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            simulationService.stopSimulation(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Simulation stopped successfully"));
        }

        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/pauseSimulation")
    public ResponseEntity<SimulationResponse> pauseSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            simulationService.pauseSimulation(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Simulation paused successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/resumeSimulation")
    public ResponseEntity<SimulationResponse> resumeSimulation(@RequestParam Long sessionId) {
        try {
            if (!simulationService.isSimulationPaused(sessionId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimulationResponse(null, "No paused simulation found"));
            }

            simulationService.resumeSimulation(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Simulation resumed successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }



    @GetMapping("/config")
    public ResponseEntity<String> getBuildingConfig(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (!building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Building is not finalized");
            }
            return ResponseEntity.ok(gson.toJson(building));
        }

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/removeFloor")
    public ResponseEntity<String> removeFloor(@RequestParam Long sessionId, @RequestParam int floorId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot modify finalized building.");
            }
            building.removeFloor(floorId);
            sessionService.updateSession(session);
            return ResponseEntity.ok("Floor removed successfully.");
        }
        catch (BuildingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(handleError(e));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(handleError(e));
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