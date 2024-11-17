package org.example.securitysystem.model.dto;

import lombok.Data;

@Data
public class BuildingRequest {
    private Long sessionId;
    private int heightInFloors;
    private double floorArea;
}
