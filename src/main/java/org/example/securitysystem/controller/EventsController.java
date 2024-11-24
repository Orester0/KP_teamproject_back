package org.example.securitysystem.controller;

import org.example.securitysystem.model.dto.SimulationResponse;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.SessionService;
import org.example.securitysystem.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventsController {
    private final SessionService sessionService;
    private final WebSocketService webSocketService;

    @Autowired
    public EventsController(SessionService sessionService, WebSocketService webSocketService) {
        this.sessionService = sessionService;
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/subscribe")
    @SendTo("/topic/events")
    public String subscribeToEvents(String message) {
        return "Subscription confirmed for: " + message;
    }

    @PostMapping("/startSimulation")
    public ResponseEntity<SimulationResponse> startSimulation(@RequestParam Long sessionId) {
        try {
            var session = sessionService.getSession(sessionId);
            Building building = session.getBuilding();

            if (!building.isFinalized()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new SimulationResponse(null, "Building must be finalized before starting simulation"));
            }

            String socketTopic = webSocketService.createTopicForSession(sessionId);
            //simulationService.startSimulation(userId, building);

            // Example of sending initial event
            webSocketService.sendSimulationEvent(socketTopic, "Simulation initialization started");

            return ResponseEntity.ok(new SimulationResponse(socketTopic, "Simulation started successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new SimulationResponse(null, "Error: " + e.getMessage()));
        }
    }
}
