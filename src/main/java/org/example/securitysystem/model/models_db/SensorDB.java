package org.example.securitysystem.model.models_db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sensor")
@Getter
@Setter
@Access(AccessType.FIELD)
public class SensorDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private long sensorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomDB room;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "status", nullable = false)
    private boolean status;
}
