package org.example.securitysystem.service.application_service.interfaces;


import org.example.securitysystem.model.db_models.SensorDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISensorRepository extends JpaRepository<SensorDB, Long> {
}
