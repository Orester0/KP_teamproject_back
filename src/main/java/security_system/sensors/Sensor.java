package security_system.sensors;

import security_system.SecurityColleague;
import security_system.SecuritySystemMediator;

public abstract class Sensor implements SecurityColleague {
    protected SecuritySystemMediator securityMediator;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public abstract  void detect();
}
