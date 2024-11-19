package org.example.securitysystem.model.models_db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private long roomId;

    @ManyToOne
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Column(name = "type")
    private String type;


}
