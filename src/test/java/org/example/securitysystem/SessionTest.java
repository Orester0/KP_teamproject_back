package org.example.securitysystem;

import org.example.securitysystem.model.entity.Session;
import org.example.securitysystem.model.entity.building.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private Session session;

    @BeforeEach
    void setUp() {
        session = new Session("Test Session");
    }

    @Test
    void testConstructorWithName() {
        // Перевіряємо, що конструктор коректно ініціалізує поле 'name'
        assertEquals("Test Session", session.getName(), "Name should be initialized correctly.");
    }

    @Test
    void testSetId() {
        // Перевіряємо, чи можна правильно встановити id
        session.setId(100L);
        assertEquals(100L, session.getId(), "ID should be set correctly.");
    }

    @Test
    void testSetIdWithNegativeValue() {
        // Перевіряємо граничний випадок, коли id є негативним значенням
        session.setId(-1L);
        assertEquals(-1L, session.getId(), "ID should be set correctly even for negative values.");
    }

    @Test
    void testSetIdWithZero() {
        // Перевіряємо граничний випадок, коли id дорівнює 0
        session.setId(0L);
        assertEquals(0L, session.getId(), "ID should be set correctly for zero.");
    }

    @Test
    void testSetBuilding() {
        // Перевіряємо, чи можна правильно встановити значення для building
        Building building = new Building(5, 2000.0);  // Тепер створюємо об'єкт з параметрами
        session.setBuilding(building);
        assertNotNull(session.getBuilding(), "Building should be set correctly.");
    }

    @Test
    void testSetBuildingNull() {
        // Перевіряємо граничний випадок, коли будівля має бути null
        session.setBuilding(null);
        assertNull(session.getBuilding(), "Building should be null.");
    }

    @Test
    void testNameNotNull() {
        // Перевіряємо, що ім'я не є null
        session.setName("Another Session");
        assertNotNull(session.getName(), "Name should not be null.");
    }

    @Test
    void testEmptyName() {
        // Перевіряємо, чи працює порожнє ім'я
        session.setName("");
        assertEquals("", session.getName(), "Name should be set as empty string.");
    }

    @Test
    void testNullName() {
        // Перевіряємо, чи може ім'я бути null
        session.setName(null);
        assertNull(session.getName(), "Name should be null.");
    }
}
