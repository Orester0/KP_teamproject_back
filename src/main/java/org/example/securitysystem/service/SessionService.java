package org.example.securitysystem.service;

import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession(String name) {
        if (sessionRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Session name is already in use");
        }
        Session newSession = new Session(name);
        return sessionRepository.save(newSession);
    }

    public Session openSession(String name) {
        Optional<Session> session = sessionRepository.findByName(name);
        if (session.isEmpty()) {
            throw new IllegalArgumentException("Name is incorrect");
        }
        return session.get();
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }


    public Session getSession(Long Id) {
        return sessionRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Session not found."));
    }

    public Building getBuildingFromSession(String name) {
        Session session = sessionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Session not found."));
        return session.getBuilding();
    }


    public void updateSession(Session session) {
        sessionRepository.save(session);
    }

    public Optional<Long> getSessionIdByName(String name) {
        return sessionRepository.findByName(name).map(Session::getId);
    }
}
