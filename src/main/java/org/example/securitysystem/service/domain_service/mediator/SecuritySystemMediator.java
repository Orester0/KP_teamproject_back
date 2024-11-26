package org.example.securitysystem.service.domain_service.mediator;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;

public interface SecuritySystemMediator {
     void notify(ISecurityColleague sender, String event) throws Exception;
     void register(ISecurityColleague colleague, String type);
}
