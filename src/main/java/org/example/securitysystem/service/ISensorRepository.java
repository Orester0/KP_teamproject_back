package org.example.securitysystem.service;


import org.example.securitysystem.model.models_db.SensorDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISensorRepository extends JpaRepository<SensorDB, Long> {
}
