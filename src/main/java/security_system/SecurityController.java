package security_system;

import java.util.HashMap;
import java.util.Map;

public class SecurityController implements SecuritySystemMediator{
    private final Map<String, SecurityColleague> colleagues = new HashMap<>();

    @Override
    public void notify(SecurityColleague sender, String event) {
        if (event.equals("Motion Detected")) {
            ((AlarmSystem)colleagues.get("AlarmSystem")).activateAlarm();
        }
    }

    @Override
    public void register(SecurityColleague colleague, String type) {
        colleagues.put(type, colleague);
    }
}
