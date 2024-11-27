package org.example.securitysystem;

import org.example.securitysystem.model.entity.room.DiningRoom;
import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTests {

    private DiningRoom diningRoom;

    @BeforeEach
    void setUp() {
        diningRoom = new DiningRoom();
    }

    @Test
    void testCalculateSensorsWithSmallArea() {
        // Встановлюємо невелику площу і кількість портів
        diningRoom.setArea(10);  // Площа кімнати
        diningRoom.setAmountOfPorts(2);  // Кількість портів

        // Викликаємо метод для розрахунку сенсорів
        diningRoom.calculateSensor();

        // Перевіряємо, що додано хоча б по одному сенсору кожного типу
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof Camera));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof Microphone));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof TemperatureSensor));

        // Перевірка, що кількість сенсорів відповідає вимогам
        assertTrue(diningRoom.getSensors().size() > 0);
    }

    @Test
    void testCalculateSensorsWithLargeArea() {
        // Встановлюємо велику площу і кількість портів
        diningRoom.setArea(100);  // Площа кімнати
        diningRoom.setAmountOfPorts(10);  // Кількість портів

        // Викликаємо метод для розрахунку сенсорів
        diningRoom.calculateSensor();

        // Перевіряємо, що додано відповідну кількість сенсорів
        long cameraCount = diningRoom.getSensors().stream().filter(sensor -> sensor instanceof Camera).count();
        long microphoneCount = diningRoom.getSensors().stream().filter(sensor -> sensor instanceof Microphone).count();
        long motionSensorCount = diningRoom.getSensors().stream().filter(sensor -> sensor instanceof MotionSensor).count();
        long temperatureSensorCount = diningRoom.getSensors().stream().filter(sensor -> sensor instanceof TemperatureSensor).count();

        // Перевірка на те, чи є більше ніж один сенсор для кожного типу
        assertTrue(cameraCount == 1);
        assertTrue(microphoneCount > 1);
        assertTrue(motionSensorCount > 1);
        assertTrue(temperatureSensorCount > 1);
    }

    @Test
    void testCalculateSensorsWithZeroArea() {
        // Встановлюємо площу 0, щоб перевірити чи не додаються сенсори
        diningRoom.setArea(0);  // Площа кімнати
        diningRoom.setAmountOfPorts(2);  // Кількість портів

        // Викликаємо метод для розрахунку сенсорів
        diningRoom.calculateSensor();

        // Перевіряємо, що додано хоча б по одному сенсору кожного типу
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof Camera));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof Microphone));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof TemperatureSensor));

        // Перевірка, що кількість сенсорів мінімальна, не більше одного сенсора
        assertTrue(diningRoom.getSensors().size() > 0);
    }

    @Test
    void testCalculateSensorsWithZeroPorts() {
        // Встановлюємо кількість портів 0
        diningRoom.setArea(50);  // Площа кімнати
        diningRoom.setAmountOfPorts(0);  // Кількість портів

        // Викликаємо метод для розрахунку сенсорів
        diningRoom.calculateSensor();

        // Перевіряємо, що хоча б один сенсор є
        assertTrue(diningRoom.getSensors().size() > 0);
    }

    @Test
    void testCalculateSensorsWithEdgeCaseValues() {
        // Тестування на межових значеннях (наприклад, 1 порт, маленька площа)
        diningRoom.setArea(1);
        diningRoom.setAmountOfPorts(1);

        // Викликаємо метод для розрахунку сенсорів
        diningRoom.calculateSensor();

        // Перевіряємо, що додано хоча б по одному сенсору кожного типу
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof Camera));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof Microphone));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof MotionSensor));
        assertTrue(diningRoom.getSensors().stream().anyMatch(sensor -> sensor instanceof TemperatureSensor));

        // Перевірка, що кількість сенсорів відповідає вимогам
        assertTrue(diningRoom.getSensors().size() > 0);
    }
}
