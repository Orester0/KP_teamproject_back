package org.example.securitysystem;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTests {

    private Building building;

    @BeforeEach
    void setUp() {
        // Створюємо будівлю висотою 3 поверхи
        building = new Building(3, 100.0);
    }

    @Test
    void testFinalizeWithZeroFloors() {
        // Граничне значення: нуль поверхів
        Exception exception = assertThrows(BuildingException.class, building::finalizeBuilding);
        assertEquals("Cannot finalize: Number of floors does not match the expected height", exception.getMessage());
    }

    @Test
    void testFinalizeWithExactFloors() throws BuildingException {
        // Граничне значення: рівно 3 поверхи
        for (int i = 0; i < 3; i++) {
            building.buildDefaultFloor();
        }

        assertDoesNotThrow(building::finalizeBuilding);
        assertTrue(building.isFinalized());
    }

    @Test
    void testFinalizeWithExcessFloors() throws BuildingException {
        // Граничне значення: перевищення висоти
        for (int i = 0; i < 3; i++) {
            building.buildDefaultFloor();
        }

        // Спроба додати ще один поверх
        Exception exception = assertThrows(BuildingException.class, building::buildDefaultFloor);
        assertEquals("Already has all floors", exception.getMessage());
    }

    @Test
    void testAddFloorAfterFinalization() throws BuildingException {
        // Додавання граничної кількості поверхів
        for (int i = 0; i < 3; i++) {
            building.buildDefaultFloor();
        }

        // Фіналізуємо будівлю
        building.finalizeBuilding();

        // Спроба додати поверх після фіналізації
        Exception exception = assertThrows(BuildingException.class, building::buildDefaultFloor);
        assertEquals("Building is finalized and cannot be modified", exception.getMessage());
    }

    @Test
    void testRemoveFloorWithInvalidIndex() throws BuildingException {
        // Граничне значення: видалення поверху, якого не існує
        Exception exception = assertThrows(BuildingException.class, () -> building.removeFloor(0));
        assertEquals("Invalid number of floor", exception.getMessage());

        // Додаємо один поверх
        building.buildDefaultFloor();

        // Спроба видалити поверх з недійсним індексом
        exception = assertThrows(BuildingException.class, () -> building.removeFloor(-1));
        assertEquals("Invalid number of floor", exception.getMessage());

        exception = assertThrows(BuildingException.class, () -> building.removeFloor(1));
        assertEquals("Invalid number of floor", exception.getMessage());
    }

    @Test
    void testRemoveFloorAfterFinalization() throws BuildingException {
        // Додаємо граничну кількість поверхів
        for (int i = 0; i < 3; i++) {
            building.buildDefaultFloor();
        }

        // Фіналізуємо будівлю
        building.finalizeBuilding();

        // Спроба видалити поверх після фіналізації
        Exception exception = assertThrows(BuildingException.class, () -> building.removeFloor(0));
        assertEquals("Building is finalized and cannot be modified", exception.getMessage());
    }
}
