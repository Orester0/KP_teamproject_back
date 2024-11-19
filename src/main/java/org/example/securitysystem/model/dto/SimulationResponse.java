package org.example.securitysystem.model.dto;

import lombok.Data;

@Data
public class SimulationResponse {
    private String socketTopic;
    private String message;

    public SimulationResponse(String socketTopic, String message) {
        this.socketTopic = socketTopic;
        this.message = message;
    }
}
