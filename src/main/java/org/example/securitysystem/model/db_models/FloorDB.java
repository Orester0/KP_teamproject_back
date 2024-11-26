package org.example.securitysystem.model.db_models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floor")
@Getter
@Setter
@Access(AccessType.FIELD)
public class FloorDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "floor_id")
    private long floorId;

    @ManyToOne(fetch = FetchType.EAGER) // Використання LAZY для оптимізації
    @JoinColumn(name = "session_id", nullable = false)
    private SessionDB session;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<RoomDB> rooms = new ArrayList<>();
}
