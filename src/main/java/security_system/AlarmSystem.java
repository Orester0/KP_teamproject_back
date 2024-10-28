package security_system;

public class AlarmSystem implements SecurityColleague{
    private SecuritySystemMediator securityMediator;

    @Override
    public void setMediator(SecuritySystemMediator mediator) {
        securityMediator = mediator;
    }

    public int activateAlarm() {
        return 1;
    }
}
