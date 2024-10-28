package security_system;

public interface SecuritySystemMediator {
     void notify(SecurityColleague sender, String event);
     void register(SecurityColleague colleague, String type);
}
