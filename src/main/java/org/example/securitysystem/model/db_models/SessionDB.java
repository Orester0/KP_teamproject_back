package org.example.securitysystem.model.db_models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session")
@Getter
@Setter
@Access(AccessType.FIELD)
public class SessionDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private long sessionId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "floor_area")
    private double floorArea;

    @Column(name = "height_in_floors")
    private int heightInFloors;

    @Column(name = "is_finalized")
    private boolean isFinalized ;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<FloorDB> floors = new ArrayList<>();
}
