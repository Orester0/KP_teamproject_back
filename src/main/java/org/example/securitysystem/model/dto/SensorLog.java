package org.example.securitysystem.model.dto;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;

import java.time.LocalDateTime;

public record SensorLog (
    SecurityColleague sensorDetails,
    boolean activated,
    LocalDateTime currentTime
){};
