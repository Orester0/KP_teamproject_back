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
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
    public String subscribeToEvents(String message, SimpMessageHeaderAccessor headerAccessor) {
        // Зберігаємо sessionId в атрибутах сесії
        headerAccessor.getSessionAttributes().put("sessionId", message);
        return "Subscription confirmed for: " + message;
    }


}
