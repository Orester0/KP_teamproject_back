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


    public static List<Object> objectList = new ArrayList<>();
    public static List<Object> objectList2 = new ArrayList<>();

    public static String buffer = "";

    // функція яка логує всі дії з сенсорами та алярмами
    @Override
    public synchronized void update(String eventType, SecurityColleague sensorDetails) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        boolean activated = true;
        LocalDateTime time  = LocalDateTime.now();
        String currentTime = time.format(formatter);
        if (eventType.endsWith("FF")) activated = false;

        SensorLogString sensorLogString;

        sensorLogString = new SensorLogString(333, activated, currentTime);

        if(sensorDetails instanceof Sensor){
            sensorLogString = new SensorLogString(((Sensor) sensorDetails).getID(), activated, currentTime);

        }

        SensorLog sensorLog = new SensorLog(sensorDetails, activated, time);
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



    public record SensorLogString(long sensorDetails, boolean activated, String currentTime) {}
    public record SensorLog(SecurityColleague sensorDetails, boolean activated, LocalDateTime currentTime) {}
}
