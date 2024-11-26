package org.example.securitysystem.service;


import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ISessionService {
    public Session createSession(String name);

    public Session openSession(String name);

    public List<Session> getAllSessions();


    public Session getSession(Long Id);

    public Building getBuildingFromSession(String name);


    public Building updateSession(Session session);

}

