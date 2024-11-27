package org.example.securitysystem;

import org.example.securitysystem.config.building_config.SecurityConfig;
import org.example.securitysystem.model.entity.room.Hall;
import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HallTests {

    // Тест для мінімальної площі
    @Test
    void testCalculateSensor_minimumArea() {
        Hall hall = new Hall(0.5, 5);  // Дуже мала площа, менша за площу одного сенсора
        hall.calculateSensor();

        // Перевірка, що хоча б один сенсор кожного типу має бути доданий
        assertTrue(hall.getSensors().size() > 0, "Повинні бути додані сенсори.");
    }

    // Тест для максимальної площі
    @Test
    void testCalculateSensor_maximumArea() {
        double maxArea = 1000.0;  // Велика площа
        Hall hall = new Hall(maxArea, 10);  // Приклад з великою площею і кількістю портів
        hall.calculateSensor();

        // Перевірка, що кількість сенсорів розраховується правильно
        int expectedCameras = Math.max(1, (int) (maxArea / SecurityConfig.HALL_CAMERA_AREA_PER_SENSOR));
        int expectedMicrophones = Math.max(1, (int) (maxArea / SecurityConfig.HALL_MICROPHONE_AREA_PER_SENSOR));
        int expectedMotionSensors = Math.max(1, 10 / SecurityConfig.HALL_MOTION_SENSOR_PORTS_PER_SENSOR); // 10 портів
        int expectedTemperatureSensors = Math.max(1, (int) (maxArea / SecurityConfig.HALL_TEMPERATURE_AREA_PER_SENSOR));

        // Тут треба перевіряти конкретні типи сенсорів, що додано
        assertTrue(hall.getSensors().stream().filter(s -> s instanceof Camera).count() == expectedCameras, "Невірна кількість камер.");
        assertTrue(hall.getSensors().stream().filter(s -> s instanceof Microphone).count() == expectedMicrophones, "Невірна кількість мікрофонів.");
        assertTrue(hall.getSensors().stream().filter(s -> s instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
        assertTrue(hall.getSensors().stream().filter(s -> s instanceof TemperatureSensor).count() == expectedTemperatureSensors, "Невірна кількість температурних датчиків.");
    }

    // Тест для мінімальної кількості портів
    @Test
    void testCalculateSensor_minimumPorts() {
        Hall hall = new Hall(50.0, 1);  // Мінімальна кількість портів
        hall.calculateSensor();

        // Перевірка, чи хоча б один датчик руху доданий, оскільки кількість портів мінімальна
        assertTrue(hall.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor), "Повинен бути хоча б один датчик руху.");
    }

    // Тест для максимальної кількості портів
    @Test
    void testCalculateSensor_maximumPorts() {
        int maxPorts = 100;  // Велика кількість портів
        Hall hall = new Hall(50.0, maxPorts);
        hall.calculateSensor();

        int expectedMotionSensors = Math.max(1, maxPorts / SecurityConfig.HALL_MOTION_SENSOR_PORTS_PER_SENSOR);

        // Перевірка, чи правильно розраховується кількість датчиків руху
        assertTrue(hall.getSensors().stream().filter(sensor -> sensor instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
    }
}
