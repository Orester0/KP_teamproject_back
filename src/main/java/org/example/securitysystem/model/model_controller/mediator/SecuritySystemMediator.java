package org.example.securitysystem.model.model_controller.mediator;

import org.example.securitysystem.model.entity.security_system.SecurityColleague;

public interface SecuritySystemMediator {
     void notify(SecurityColleague sender, String event);
     void register(SecurityColleague colleague, String type);
}
