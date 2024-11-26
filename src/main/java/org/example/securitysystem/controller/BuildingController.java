package org.example.securitysystem.controller;

import com.google.gson.Gson;
import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.dto.*;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.application_service.FloorCommandRegistry;
import org.example.securitysystem.service.domain_service.command.IAddFloorCommand;
import org.example.securitysystem.service.application_service.implementations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/building")
public class BuildingController {

    private final Gson gson;
    private final SessionService sessionService;
    private final WebSocketService webSocketService;
    private final SimulationService simulationService;
    private final ReplayService replayService;


    @Autowired
    public BuildingController(SessionService sessionService, WebSocketService webSocketService, SimulationService simulationService, ReplayService replayService) {
        this.sessionService = sessionService;
        this.webSocketService = webSocketService;
        this.simulationService = simulationService;
        this.replayService = replayService;
        this.gson = new Gson();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createBuilding(@RequestBody BuildingRequest request) {
        try {
            var session = sessionService.getSession(request.getSessionId());
            if (session == null) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Session not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }
            if (session.getBuilding() != null) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Session already has a building associated.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }

            Building building = new Building(request.getHeightInFloors(), request.getFloorArea());
            session.setBuilding(building);
            sessionService.updateSession(session);

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Building created successfully with " + request.getHeightInFloors() +
                    " floors and floor area of " + request.getFloorArea() + " sqm.");

            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(messageResponse));
        }
        catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(messageResponse));
        }
    }

    @PostMapping("/add-floor")
    public ResponseEntity<String> addFloor(@RequestParam Long sessionId, @RequestParam String floorType) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            if (building.isFinalized()) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Cannot modify finalized building.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }

            FloorCommandRegistry commandRegistry = new FloorCommandRegistry();
            IAddFloorCommand command = commandRegistry.getCommand(floorType);

            if (command == null) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Invalid floor type. Allowed values are: default, office, hostel.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }

            command.execute(building);
            sessionService.updateSession(session);

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage(floorType + " floor added successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(messageResponse));
        } catch (BuildingException e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
        } catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(messageResponse));
        }
    }

    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeBuilding(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Building is already finalized.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }
            building.finalizeBuilding();

            sessionService.updateSession(session);

            var updatedSession = sessionService.getSession(sessionId);
            Building updatedBuilding = getBuildingFromSession(updatedSession);

            return ResponseEntity.ok(gson.toJson(updatedBuilding));
        }
        catch (BuildingException e)
        {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
        }
        catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(messageResponse));
        }
    }

    @GetMapping("/config")
    public ResponseEntity<String> getBuildingConfig(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (!building.isFinalized()) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Building is not finalized");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }
            return ResponseEntity.ok(gson.toJson(building));
        }
        catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(messageResponse));
        }
    }

    @PostMapping("/start-replay")
    public ResponseEntity<SimulationResponse> replayStart(
            @RequestParam Long sessionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            if (!building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimulationResponse(null, "Building must be finalized before starting replay"));
            }

            String socketTopic = webSocketService.createTopicForSession(sessionId);
            replayService.startReplay(sessionId, building, socketTopic, startTime, endTime);

            return ResponseEntity.ok(new SimulationResponse(socketTopic, "Replay started successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }


    @PostMapping("/stop-replay")
    public ResponseEntity<SimulationResponse> replayStop(
            @RequestParam Long sessionId) {
        try {
            replayService.stopReplay(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Replay stopped successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/start-simulation")
    public ResponseEntity<SimulationResponse> startSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            if (!building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimulationResponse(null, "Building must be finalized before starting simulation"));
            }

            String socketTopic = webSocketService.createTopicForSession(sessionId);
            simulationService.start(sessionId, building, socketTopic);

            return ResponseEntity.ok(new SimulationResponse(socketTopic, "Simulation started successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }

    }

    @PostMapping("/stop-simulation")
    public ResponseEntity<SimulationResponse> stopSimulation(@RequestParam Long sessionId) {
        try {
            simulationService.stop(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Simulation stopped successfully"));
        }

        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/pause-simulation")
    public ResponseEntity<SimulationResponse> pauseSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);

            simulationService.pause(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Simulation paused successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @PostMapping("/resume-simulation")
    public ResponseEntity<SimulationResponse> resumeSimulation(@RequestParam Long sessionId) {
        try {
            if (!simulationService.isSimulationPaused(sessionId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimulationResponse(null, "No paused simulation found"));
            }

            simulationService.resume(sessionId);
            return ResponseEntity.ok(new SimulationResponse(null, "Simulation resumed successfully"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/remove-floor")
    public ResponseEntity<String> removeFloor(@RequestParam Long sessionId, @RequestParam int numberOfFloor) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = getBuildingFromSession(session);
            if (building.isFinalized()) {
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessage("Cannot modify finalized building.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
            }
            building.removeFloor(numberOfFloor);
            sessionService.updateSession(session);

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Floor removed successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(messageResponse));
        }
        catch (BuildingException e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(gson.toJson(messageResponse));
        }
        catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(gson.toJson(messageResponse));
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