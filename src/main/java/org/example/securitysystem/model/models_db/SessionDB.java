package org.example.securitysystem.model.models_db;


import jakarta.persistence.*;

@Entity
@Table(name = "session")
public class Session  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long session_id;

    @Column( unique = true)
    private String name;
}
