package org.example.securitysystem.model.dto;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class SensorLog implements Serializable {
    SecurityColleague sensorDetails;
    boolean activated;
    String currentTime;
}
