package org.example.securitysystem;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.DiningRoom;
import org.example.securitysystem.model.entity.room.Hall;
import org.example.securitysystem.model.entity.room.Kitchen;
import org.example.securitysystem.model.entity.room.LivingRoom;
import org.example.securitysystem.model.entity.room.Office;
import org.example.securitysystem.model.entity.room.WC;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FloorTests {

    private Floor floor;

    @BeforeEach
    void setUp() {
        floor = new Floor();
        floor.setFloorNumber(0);
    }

    @Test
    void testAddRoomToEmptyFloor() {
        // Перевіряємо, що спочатку кімнат немає
        assertTrue(floor.getRooms().isEmpty());

        // Додаємо DiningRoom як першу кімнату
        DiningRoom diningRoom = new DiningRoom();
        floor.addRoom(diningRoom);

        // Перевіряємо, що DiningRoom додався
        assertEquals(1, floor.getRooms().size());
        assertSame(diningRoom, floor.getRooms().get(0));
    }

    @Test
    void testAddMultipleRooms() {
        // Додаємо кілька кімнат
        DiningRoom diningRoom = new DiningRoom();
        Hall hall = new Hall();
        Kitchen kitchen = new Kitchen();

        floor.addRoom(diningRoom);
        floor.addRoom(hall);
        floor.addRoom(kitchen);

        // Перевіряємо кількість доданих кімнат
        assertEquals(3, floor.getRooms().size());
        assertSame(diningRoom, floor.getRooms().get(0));
        assertSame(hall, floor.getRooms().get(1));
        assertSame(kitchen, floor.getRooms().get(2));
    }

    @Test
    void testAddNullRoom() {
        // Спроба додати `null` кімнату
        DiningRoom room = null;
        floor.addRoom(room);  // За умовчанням можемо додавати конкретні типи кімнат

        // Перевіряємо, що кімната `null` додана
        assertEquals(1, floor.getRooms().size());
        assertNull(floor.getRooms().get(0));
    }

    @Test
    void testGetSensorsFromEmptyFloor() {
        // Перевіряємо, що на порожньому поверсі немає сенсорів
        List<Sensor> sensors = floor.getSensors();
        assertTrue(sensors.isEmpty());
    }

    @Test
    void testGetSensorsFromRooms() {
        // Створюємо кімнати з сенсорами
        DiningRoom diningRoom = new DiningRoom();
        Kitchen kitchen = new Kitchen();

        Sensor sensor1 = new TemperatureSensor();
        Sensor sensor2 = new MotionSensor();

        diningRoom.addSensor(sensor1);
        kitchen.addSensor(sensor2);

        floor.addRoom(diningRoom);
        floor.addRoom(kitchen);

        // Отримуємо список сенсорів з поверху
        List<Sensor> sensors = floor.getSensors();

        // Перевіряємо, що сенсори отримані коректно
        assertEquals(2, sensors.size());
        assertTrue(sensors.contains(sensor1));
        assertTrue(sensors.contains(sensor2));
    }

    @Test
    void testGetSensorsWithEmptyRooms() {
        // Додаємо кімнати без сенсорів
        LivingRoom livingRoom = new LivingRoom();
        WC wc = new WC();

        floor.addRoom(livingRoom);
        floor.addRoom(wc);

        // Перевіряємо, що сенсори відсутні
        List<Sensor> sensors = floor.getSensors();
        assertTrue(sensors.isEmpty());
    }

    @Test
    void testSetFloorNumber() {
        // Перевіряємо початковий номер поверху
        assertEquals(0, floor.getFloorNumber());

        // Встановлюємо новий номер
        floor.setFloorNumber(5);

        // Перевіряємо новий номер
        assertEquals(5, floor.getFloorNumber());
    }
}
