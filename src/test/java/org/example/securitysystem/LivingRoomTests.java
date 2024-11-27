package org.example.securitysystem;

import org.example.securitysystem.config.building_config.SecurityConfig;
import org.example.securitysystem.model.entity.room.LivingRoom;
import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LivingRoomTests {

    // Тест для мінімальної площі
    @Test
    void testCalculateSensor_minimumArea() {
        LivingRoom livingRoom = new LivingRoom(0.5, 5);  // Дуже мала площа, менша за площу одного сенсора
        livingRoom.calculateSensor();

        // Перевірка, що хоча б один сенсор кожного типу має бути доданий
        assertTrue(livingRoom.getSensors().size() > 0, "Повинні бути додані сенсори.");
    }

    // Тест для максимальної площі
    @Test
    void testCalculateSensor_maximumArea() {
        double maxArea = 1000.0;  // Велика площа
        LivingRoom livingRoom = new LivingRoom(maxArea, 10);  // Приклад з великою площею і кількістю портів
        livingRoom.calculateSensor();

        // Перевірка, що кількість сенсорів розраховується правильно
        int expectedCameras = Math.max(1, (int) (maxArea / SecurityConfig.LIVINGROOM_CAMERA_AREA_PER_SENSOR));
        int expectedMicrophones = Math.max(1, (int) (maxArea / SecurityConfig.LIVINGROOM_MICROPHONE_AREA_PER_SENSOR));
        int expectedMotionSensors = Math.max(1, 10 / SecurityConfig.LIVINGROOM_MOTION_SENSOR_PORTS_PER_SENSOR); // 10 портів
        int expectedTemperatureSensors = Math.max(1, (int) (maxArea / SecurityConfig.LIVINGROOM_TEMPERATURE_AREA_PER_SENSOR));

        // Перевірка правильності кількості сенсорів
        assertTrue(livingRoom.getSensors().stream().filter(s -> s instanceof Camera).count() == expectedCameras, "Невірна кількість камер.");
        assertTrue(livingRoom.getSensors().stream().filter(s -> s instanceof Microphone).count() == expectedMicrophones, "Невірна кількість мікрофонів.");
        assertTrue(livingRoom.getSensors().stream().filter(s -> s instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
        assertTrue(livingRoom.getSensors().stream().filter(s -> s instanceof TemperatureSensor).count() == expectedTemperatureSensors, "Невірна кількість температурних датчиків.");
    }

    // Тест для мінімальної кількості портів
    @Test
    void testCalculateSensor_minimumPorts() {
        LivingRoom livingRoom = new LivingRoom(50.0, 1);  // Мінімальна кількість портів
        livingRoom.calculateSensor();

        // Перевірка, чи хоча б один датчик руху доданий, оскільки кількість портів мінімальна
        assertTrue(livingRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor), "Повинен бути хоча б один датчик руху.");
    }

    // Тест для максимальної кількості портів
    @Test
    void testCalculateSensor_maximumPorts() {
        int maxPorts = 100;  // Велика кількість портів
        LivingRoom livingRoom = new LivingRoom(50.0, maxPorts);
        livingRoom.calculateSensor();

        int expectedMotionSensors = Math.max(1, maxPorts / SecurityConfig.LIVINGROOM_MOTION_SENSOR_PORTS_PER_SENSOR);

        // Перевірка, чи правильно розраховується кількість датчиків руху
        assertTrue(livingRoom.getSensors().stream().filter(sensor -> sensor instanceof MotionSensor).count() == expectedMotionSensors, "Невірна кількість датчиків руху.");
    }
}
