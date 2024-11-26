package org.example.securitysystem.service.interfaces;

import org.example.securitysystem.model.models_db.AlarmLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlarmRepository extends JpaRepository<AlarmLog, Long> {
}
