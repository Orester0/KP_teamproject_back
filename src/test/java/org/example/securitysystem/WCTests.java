package org.example.securitysystem;

import org.example.securitysystem.config.building_config.SecurityConfig;
import org.example.securitysystem.model.entity.room.WC;
import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WCTests {

    // Тест для мінімальної площі
    @Test
    void testCalculateSensor_minimumArea() {
        WC wc = new WC(0.5, 2);  // Дуже мала площа, менша за площу одного сенсора
        wc.calculateSensor();

        // Перевірка, що хоча б один сенсор кожного типу має бути доданий
        assertTrue(wc.getSensors().size() > 0, "Повинні бути додані сенсори.");
    }

    // Тест для великої площі
    @Test
    void testCalculateSensor_largeArea() {
        double largeArea = 100.0;  // Велика площа
        WC wc = new WC(largeArea, 5);  // Приклад з великою площею і кількістю портів
        wc.calculateSensor();

        // Перевірка, що кількість сенсорів розраховується правильно
        int expectedCameras = Math.max(1, (int) (largeArea / SecurityConfig.WC_CAMERA_AREA_PER_SENSOR));
        int expectedMicrophones = Math.max(1, (int) (largeArea / SecurityConfig.WC_MICROPHONE_AREA_PER_SENSOR));
        int expectedMotionSensors = Math.max(1, 5 / SecurityConfig.WC_MOTION_SENSOR_PORTS_PER_SENSOR); // 5 портів
        int expectedTemperatureSensors = Math.max(1, (int) (largeArea / SecurityConfig.WC_TEMPERATURE_AREA_PER_SENSOR));

        // Перевірка правильності кількості сенсорів
        assertTrue(wc.getSensors().stream().filter(s -> s instanceof Camera).count() == expectedCameras, "Невірна кількість камер.");
        assertTrue(wc.getSensors().stream().filter(s -> s instanceof Microphone).count() == expectedMicrophones, "Невірна кількість мікрофонів.");
        assertTrue(wc.getSensors().stream().filter(s -> s instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
        assertTrue(wc.getSensors().stream().filter(s -> s instanceof TemperatureSensor).count() == expectedTemperatureSensors, "Невірна кількість температурних датчиків.");
    }

    // Тест для мінімальної кількості портів
    @Test
    void testCalculateSensor_minimumPorts() {
        WC wc = new WC(10.0, 1);  // Мінімальна кількість портів
        wc.calculateSensor();

        // Перевірка, чи хоча б один датчик руху доданий
        assertTrue(wc.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor), "Повинен бути хоча б один датчик руху.");
    }

    // Тест для максимальної кількості портів
    @Test
    void testCalculateSensor_maximumPorts() {
        int maxPorts = 50;  // Велика кількість портів
        WC wc = new WC(10.0, maxPorts);
        wc.calculateSensor();

        int expectedMotionSensors = Math.max(1, maxPorts / SecurityConfig.WC_MOTION_SENSOR_PORTS_PER_SENSOR);

        // Перевірка, чи правильно розраховується кількість датчиків руху
        assertTrue(wc.getSensors().stream().filter(sensor -> sensor instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
    }
}
