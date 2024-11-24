package org.example.securitysystem.mappers;

import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SensorFactory {

    private static final Map<String, Class<? extends Sensor>> sensorRegistry = new HashMap<>();

    static {
        Set<Class<? extends Sensor>> sensorClasses = findSubclasses(Sensor.class);
        for (Class<? extends Sensor> sensorClass : sensorClasses) {

            try {
                String sensorType = sensorClass.getSimpleName();
                sensorRegistry.put(sensorType, sensorClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Set<Class<? extends Sensor>> findSubclasses(Class<Sensor> superClass) {

        Reflections reflections = new Reflections("org.example.securitysystem.model.entity.security_system.sensors");
        return reflections.getSubTypesOf(superClass);
    }

    public static Sensor createSensor(String sensorType) throws Exception {
        Class<? extends Sensor> sensorClass = sensorRegistry.get(sensorType);
        if (sensorClass != null) {
            try {
                return sensorClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new Exception("Error creating sensor of type " + sensorType, e);
            }
        } else {
            throw new Exception("Unknown sensor type: " + sensorType);
        }
    }
}

