package org.example.securitysystem.service.application_service.interfaces;

import org.example.securitysystem.model.db_models.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ILogRepository extends JpaRepository<EventLog, Long>, JpaSpecificationExecutor<EventLog> {
    List<EventLog> findBySensor_SensorId(long sensorId);
}
