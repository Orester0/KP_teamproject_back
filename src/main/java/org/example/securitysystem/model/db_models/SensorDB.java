package org.example.securitysystem.model.db_models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @OneToMany(mappedBy = "sensor")
    private List<EventLog> eventLogs;
}
