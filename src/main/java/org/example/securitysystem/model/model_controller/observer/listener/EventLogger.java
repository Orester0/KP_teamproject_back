package org.example.securitysystem.model.model_controller.observer.listener;

import lombok.Data;
import org.example.securitysystem.model.entity.security_system.SecurityColleague;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventLogger implements SecurityEventListener {


    public static List<Object> objectList = new ArrayList<>();
    public static List<Object> objectList2 = new ArrayList<>();

    public static String buffer = "";

    // функція яка логує всі дії з сенсорами та алярмами
    @Override
    public synchronized void update(String eventType, SecurityColleague sensorDetails) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        boolean activated = true;
        String currentTime = now.format(formatter);

        if (eventType.endsWith("FF")) activated = false;

        SensorLogString sensorLogString = new SensorLogString(sensorDetails.getClass().getSimpleName(), activated, currentTime);
        SensorLog sensorLog = new SensorLog(sensorDetails, activated, currentTime);
        buffer += sensorLogString + "\n";


        // Додаємо до списку Object
        objectList.add(sensorLog);
        objectList2.add(sensorLog);
    }


    public synchronized String getBuffer() {
        return buffer;
    }

    public synchronized void clearBuffer() {
        buffer = "";
    }

    public synchronized List<Object> getObjectList() {
        return objectList;
    }

    public synchronized void clearObjectList() {
        objectList.clear();
    }



    public record SensorLogString(String sensorDetails, boolean activated, String currentTime) {}
    public record SensorLog(SecurityColleague sensorDetails, boolean activated, String currentTime) {}
}
