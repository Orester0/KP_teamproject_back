package org.example.securitysystem.service.application_service.interfaces;

import org.example.securitysystem.model.entity.building.Building;

public interface ISimulationService {
    void start(Long sessionID, Building building, String socketTopic);
    void resume(Long sessionId);
    void pause(Long sessionId);
    void stop(Long sessionId);
}
