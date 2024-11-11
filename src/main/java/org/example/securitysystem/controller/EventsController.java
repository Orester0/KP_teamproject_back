package org.example.securitysystem.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EventsController {

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
