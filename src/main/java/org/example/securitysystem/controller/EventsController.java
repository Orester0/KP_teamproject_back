package org.example.securitysystem.controller;

import org.example.securitysystem.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EventsController {
    private final SessionService sessionService;

    @Autowired
    public EventsController(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @MessageMapping("/subscribe")
    @SendTo("/topic/events")
    public String subscribeToEvents(String message) {
        // This is a simple confirmation message, customize as needed
        return "Subscription confirmed for: " + message;
    }

    public void broadcastEvent(String event) {
        // This method can be called by other parts of the application to broadcast new events
        // A SimpMessagingTemplate bean would be used here to send messages to subscribers.
    }
}
