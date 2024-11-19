package org.example.securitysystem.model.models_db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long floor_id;

    @ManyToOne
    @Column(name = "session_id")
    @JoinColumn(name = "session_id")
    private Session session;

    private String type;

    @Column(name = "floor_number")
    private int floorNumber;


}
