package org.example.securitysystem.model.model_controller.observer.listener;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventLogger implements SecurityEventListener {
    // LOG TO DATABASE



    // список об'єктів з якими щось відбулось
    public static List<SecurityColleague> list = new ArrayList<>();

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
        list.add(sensorDetails);
    }

    public String getBuffer() {

        return buffer;
    }


    // окремий потік який періодично загружає буфер у базу даних
    private class DatabaseLogger implements Runnable {

        @Override
        public void run() {


        }
    }
}
