package org.example.securitysystem.service;


import jakarta.transaction.Transactional;
import org.example.securitysystem.mappers.SessionMapper;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.models_db.SessionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService implements ISessionService {
    private final ISessionRepository sessionRepository;

    @Autowired
    public SessionService( ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    @Transactional
    public Session createSession(String name) {
        if (sessionRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Session name is already in use");
        }
        Session newSession = new Session(name);
        try {
            SessionDB savedSessionDB = sessionRepository.save(SessionMapper.mapToSessionDB(newSession));

            return SessionMapper.mapToSession(savedSessionDB);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map SessionDB to Session", e);
        }
    }

    public Session openSession(String name) {
        Optional<SessionDB> session = sessionRepository.findByName(name);
        if (session.isEmpty()) {
            throw new IllegalArgumentException("Name is incorrect");
        }
        SessionDB sessionDB = session.orElse(null);

        try {
            return SessionMapper.mapToSession(sessionDB);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map SessionDB to Session", e);
        }
    }


    public List<Session> getAllSessions() {
        List<SessionDB> sessionsDB = sessionRepository.findAll();
        List<Session> sessions = new ArrayList<>();
        for(SessionDB sessionDB: sessionsDB){

            try {
                sessions.add(SessionMapper.mapToSession(sessionDB));
            } catch (Exception e) {
                throw new RuntimeException("Failed to map SessionDB to Session", e);
            }
        }
        return sessions;
    }


    public Session getSession(Long id) {
        try {
            SessionDB sessionDB = sessionRepository
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException("Session not found."));

            return SessionMapper.mapToSession(sessionDB);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map SessionDB to Session", e);
        }
    }


    public Building getBuildingFromSession(String name) {
        try{
        SessionDB session = sessionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Session not found."));
        return SessionMapper.mapToSession(session).getBuilding();
    } catch (Exception e) {
        throw new RuntimeException("Failed to map SessionDB to Session", e);
    }
    }
    @Transactional
    @Override
    public void updateSession(Session session) {
        SessionDB sessionDB = sessionRepository.findById(session.getId())
                .orElseThrow(() -> new RuntimeException("Session not found."));
        try{
        SessionDB newSession = SessionMapper.mapToSessionDB(session);
            sessionRepository.save(newSession);
        }
        //return building
        catch (Exception e) {
        throw new RuntimeException("Failed to map SessionDB to Session", e);
    }
    }

    @Override
    public Optional<Long> getSessionIdByName(String name) {
        return Optional.empty();
    }


    public void updateSession(SessionDB session) {
        sessionRepository.save(session);
    }

//    public Optional<Long> getSessionIdByName(String name) {
//        return sessionRepository.findByName(name).map(SessionDB::getId);
//    }
}
