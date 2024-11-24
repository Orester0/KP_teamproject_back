package org.example.securitysystem.model.entity.security_system;

import org.example.securitysystem.service.LogService;

public interface Loggable {
    public void log(LogService logService,boolean status);
}
