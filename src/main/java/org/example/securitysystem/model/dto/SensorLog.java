package org.example.securitysystem.model.dto;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.ISecurityColleague;

import java.io.Serializable;

@Data
public class SensorLog implements Serializable {
    ISecurityColleague sensorDetails;
    boolean activated;
    String currentTime;
}
