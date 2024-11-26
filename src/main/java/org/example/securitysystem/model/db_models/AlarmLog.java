package org.example.securitysystem.model.db_models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
@Entity
@Table(name = "alarm_log")
@Getter
@Setter
public class AlarmLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId;

    @Column(name = "alarm_type")
    private String type;

    @Column(name = "status")
    private boolean status;

    @Column(name = "time")
    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false)
    private SessionDB session;
}
