package org.example.securitysystem;

import org.example.securitysystem.config.building_config.SecurityConfig;
import org.example.securitysystem.model.entity.room.Kitchen;
import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KitchenTests {

    // Тест для мінімальної площі
    @Test
    void testCalculateSensor_minimumArea() {
        Kitchen kitchen = new Kitchen(0.5, 5);  // Дуже мала площа, менша за площу одного сенсора
        kitchen.calculateSensor();

        // Перевірка, що хоча б один сенсор кожного типу має бути доданий
        assertTrue(kitchen.getSensors().size() > 0, "Повинні бути додані сенсори.");
    }

    // Тест для максимальної площі
    @Test
    void testCalculateSensor_maximumArea() {
        double maxArea = 1000.0;  // Велика площа
        Kitchen kitchen = new Kitchen(maxArea, 10);  // Приклад з великою площею і кількістю портів
        kitchen.calculateSensor();

        // Перевірка, що кількість сенсорів розраховується правильно
        int expectedCameras = Math.max(1, (int) (maxArea / SecurityConfig.KITCHEN_CAMERA_AREA_PER_SENSOR));
        int expectedMicrophones = Math.max(1, (int) (maxArea / SecurityConfig.KITCHEN_MICROPHONE_AREA_PER_SENSOR));
        int expectedMotionSensors = Math.max(1, 10 / SecurityConfig.KITCHEN_MOTION_SENSOR_PORTS_PER_SENSOR); // 10 портів
        int expectedTemperatureSensors = Math.max(1, (int) (maxArea / SecurityConfig.KITCHEN_TEMPERATURE_AREA_PER_SENSOR));

        // Перевірка правильності кількості сенсорів
        assertTrue(kitchen.getSensors().stream().filter(s -> s instanceof Camera).count() == expectedCameras, "Невірна кількість камер.");
        assertTrue(kitchen.getSensors().stream().filter(s -> s instanceof Microphone).count() == expectedMicrophones, "Невірна кількість мікрофонів.");
        assertTrue(kitchen.getSensors().stream().filter(s -> s instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
        assertTrue(kitchen.getSensors().stream().filter(s -> s instanceof TemperatureSensor).count() == expectedTemperatureSensors, "Невірна кількість температурних датчиків.");
    }

    // Тест для мінімальної кількості портів
    @Test
    void testCalculateSensor_minimumPorts() {
        Kitchen kitchen = new Kitchen(50.0, 1);  // Мінімальна кількість портів
        kitchen.calculateSensor();

        // Перевірка, чи хоча б один датчик руху доданий, оскільки кількість портів мінімальна
        assertTrue(kitchen.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor), "Повинен бути хоча б один датчик руху.");
    }

    // Тест для максимальної кількості портів
    @Test
    void testCalculateSensor_maximumPorts() {
        int maxPorts = 100;  // Велика кількість портів
        Kitchen kitchen = new Kitchen(50.0, maxPorts);
        kitchen.calculateSensor();

        int expectedMotionSensors = Math.max(1, maxPorts / SecurityConfig.KITCHEN_MOTION_SENSOR_PORTS_PER_SENSOR);

        // Перевірка, чи правильно розраховується кількість датчиків руху
        assertTrue(kitchen.getSensors().stream().filter(sensor -> sensor instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
    }
}
