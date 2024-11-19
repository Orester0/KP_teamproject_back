package org.example.securitysystem.service;

import org.example.securitysystem.model.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByName(String username);
}
