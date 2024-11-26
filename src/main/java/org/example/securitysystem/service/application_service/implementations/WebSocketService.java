package org.example.securitysystem.service.application_service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String createTopicForSession(Long sessionId) {
        return "/topic/simulation/" + sessionId + "/" + UUID.randomUUID();
    }

    public void sendSimulationEvent(String topic, Object event) {
        messagingTemplate.convertAndSend(topic, event);
    }
}


