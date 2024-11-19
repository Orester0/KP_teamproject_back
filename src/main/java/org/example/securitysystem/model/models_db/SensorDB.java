package org.example.securitysystem.model.models_db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sensors")
@Getter
@Setter
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private long roomId;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Floor floor;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private boolean status;
}
