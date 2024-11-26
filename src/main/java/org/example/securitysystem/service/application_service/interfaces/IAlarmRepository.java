package org.example.securitysystem.service.application_service.interfaces;

import org.example.securitysystem.model.db_models.AlarmLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAlarmRepository extends JpaRepository<AlarmLog, Long> {
}
