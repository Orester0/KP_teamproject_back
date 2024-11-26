package org.example.securitysystem.model.db_models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
@Getter
@Setter
@Access(AccessType.FIELD)
public class RoomDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private long roomId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", nullable = false)
    private FloorDB floor;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "area", nullable = false)
    private double area;

    @Column(name = "amount_of_ports", nullable = false)
    private int amountOfPorts;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SensorDB> sensors = new ArrayList<>();
}
