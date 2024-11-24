package org.example.securitysystem.model.dto;

import lombok.Data;

@Data
public class SimulationResponse {
    private String topic;
    private String message;

    public SimulationResponse(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }
}