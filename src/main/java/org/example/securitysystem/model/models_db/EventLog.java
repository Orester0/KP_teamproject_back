package org.example.securitysystem.model.models_db;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.time.LocalDateTime;

@Table(name = "event_log")
@Getter
@Setter

public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId;
    private Sensor sensorId;
    private LocalDateTime startTime;

}
