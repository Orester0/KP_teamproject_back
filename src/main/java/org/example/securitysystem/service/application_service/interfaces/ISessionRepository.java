package org.example.securitysystem.service.application_service.interfaces;


import org.example.securitysystem.model.db_models.SessionDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISessionRepository extends JpaRepository<SessionDB, Long> {
    Optional<SessionDB> findByName(String username);
}
