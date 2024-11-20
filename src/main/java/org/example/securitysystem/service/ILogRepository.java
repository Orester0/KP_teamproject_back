package org.example.securitysystem.service;

import org.example.securitysystem.model.models_db.EventLog;
import org.example.securitysystem.model.models_db.SessionDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ILogRepository extends JpaRepository<EventLog, Long>, JpaSpecificationExecutor<EventLog> {
    List<EventLog> findBySensor_SensorId(long sensorId);
}
