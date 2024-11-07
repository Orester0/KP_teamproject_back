package org.example.securitysystem.model.entity.security_system;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SensorIDGenerator {
    private static final Map<String, Map<String, AtomicInteger>> roomSensorCounters = new HashMap<>();

    public static String generateSensorID(int floorNumber, String roomId, String sensorType) {
        // Get the counter map for the current room, or create a new one if it doesn't exist
        Map<String, AtomicInteger> roomCounters = roomSensorCounters.computeIfAbsent(roomId, k -> new HashMap<>());

        // Get the counter for the current sensor type, or create a new one if it doesn't exist
        AtomicInteger counter = roomCounters.computeIfAbsent(sensorType, k -> new AtomicInteger(1));

        // Generate the sensor ID in the format [AA][B][CCC]
        return String.format("%02d%s%03d", floorNumber, sensorType, counter.getAndIncrement());
    }
}
