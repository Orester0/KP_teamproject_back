package org.example.securitysystem;

import org.example.securitysystem.config.building_config.SecurityConfig;
import org.example.securitysystem.model.entity.room.Office;
import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OfficeTests {

    // Тест для мінімальної площі
    @Test
    void testCalculateSensor_minimumArea() {
        Office office = new Office(0.5, 5);  // Дуже мала площа, менша за площу одного сенсора
        office.calculateSensor();

        // Перевірка, що хоча б один сенсор кожного типу має бути доданий
        assertTrue(office.getSensors().size() > 0, "Повинні бути додані сенсори.");
    }

    // Тест для максимальної площі
    @Test
    void testCalculateSensor_maximumArea() {
        double maxArea = 1000.0;  // Велика площа
        Office office = new Office(maxArea, 10);  // Приклад з великою площею і кількістю портів
        office.calculateSensor();

        // Перевірка, що кількість сенсорів розраховується правильно
        int expectedCameras = Math.max(1, (int) (maxArea / SecurityConfig.OFFICE_CAMERA_AREA_PER_SENSOR));
        int expectedMicrophones = Math.max(1, (int) (maxArea / SecurityConfig.OFFICE_MICROPHONE_AREA_PER_SENSOR));
        int expectedMotionSensors = Math.max(1, 10 / SecurityConfig.OFFICE_MOTION_SENSOR_PORTS_PER_SENSOR); // 10 портів
        int expectedTemperatureSensors = Math.max(1, (int) (maxArea / SecurityConfig.OFFICE_TEMPERATURE_AREA_PER_SENSOR));

        // Перевірка правильності кількості сенсорів
        assertTrue(office.getSensors().stream().filter(s -> s instanceof Camera).count() == expectedCameras, "Невірна кількість камер.");
        assertTrue(office.getSensors().stream().filter(s -> s instanceof Microphone).count() == expectedMicrophones, "Невірна кількість мікрофонів.");
        assertTrue(office.getSensors().stream().filter(s -> s instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
        assertTrue(office.getSensors().stream().filter(s -> s instanceof TemperatureSensor).count() == expectedTemperatureSensors, "Невірна кількість температурних датчиків.");
    }

    // Тест для мінімальної кількості портів
    @Test
    void testCalculateSensor_minimumPorts() {
        Office office = new Office(50.0, 1);  // Мінімальна кількість портів
        office.calculateSensor();

        // Перевірка, чи хоча б один датчик руху доданий, оскільки кількість портів мінімальна
        assertTrue(office.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor), "Повинен бути хоча б один датчик руху.");
    }

    // Тест для максимальної кількості портів
    @Test
    void testCalculateSensor_maximumPorts() {
        int maxPorts = 100;  // Велика кількість портів
        Office office = new Office(50.0, maxPorts);
        office.calculateSensor();

        int expectedMotionSensors = Math.max(1, maxPorts / SecurityConfig.OFFICE_MOTION_SENSOR_PORTS_PER_SENSOR);

        // Перевірка, чи правильно розраховується кількість датчиків руху
        assertTrue(office.getSensors().stream().filter(sensor -> sensor instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
    }
}
