package org.example.securitysystem.service.application_service.implementations;


import jakarta.transaction.Transactional;
import org.example.securitysystem.mappers.SessionMapper;
import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.db_models.SessionDB;
import org.example.securitysystem.service.application_service.interfaces.ISessionRepository;
import org.example.securitysystem.service.application_service.interfaces.ISessionService;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new RuntimeException(e.getMessage(), e);
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
    public Building updateSession(Session session) {
        SessionDB sessionDB = sessionRepository.findById(session.getId())
                .orElseThrow(() -> new RuntimeException("Session not found."));
        try{
            SessionDB newSession = SessionMapper.mapToSessionDB(session);
            System.out.println(newSession.getFloors().toString());
            sessionRepository.save(newSession);
            return getBuildingFromSession(session.getName());
        }

        catch (Exception e) {
        throw new RuntimeException("Failed to map SessionDB to Session", e);
    }
    }




}
