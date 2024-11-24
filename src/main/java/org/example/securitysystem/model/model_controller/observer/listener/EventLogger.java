package org.example.securitysystem.model.model_controller.observer.listener;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.Loggable;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class EventLogger implements SecurityEventListener {
    // LOG TO DATABASE

    @Autowired
    private LogService eventLoggingService;

    // список об'єктів з якими щось відбулось
    public static Map<SecurityColleague, Boolean> eventMap = new HashMap<>();


    public static String buffer = "";


    // функція яка логує всі дії з сенсорами та алярмами
    @Override
    public synchronized void update(String eventType, SecurityColleague sensorDetails) {
        //var sensorLog = new {sensorDetails, activated, datetime};
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        buffer += sensorDetails.getClass().getSimpleName() + " was activated on " + now.format(formatter);

        if (sensorDetails instanceof Sensor sensor) {
            buffer += " and hash " + sensor.getID() + "\n";
        } else {
            buffer += "\n";
        }
        eventMap.put(sensorDetails, true);//to do
    }

    public String getBuffer() {

        return buffer;
    }

    @Async
    @Scheduled(fixedRate = 5000) // кожні 5 секунд
    public void logEventsPeriodically() {
        if (!eventMap.isEmpty()) {
            eventMap.forEach((key, value) -> {
                ((Loggable)key).log(eventLoggingService,value);
            });

            eventMap.clear();
        }
    }
}
