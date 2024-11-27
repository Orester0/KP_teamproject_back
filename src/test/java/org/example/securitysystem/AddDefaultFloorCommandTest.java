package org.example.securitysystem;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.domain_service.command.AddDefaultFloorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddDefaultFloorCommandTest {

    private AddDefaultFloorCommand addDefaultFloorCommand;
    private Building mockBuilding;

    @BeforeEach
    void setUp() {
        addDefaultFloorCommand = new AddDefaultFloorCommand();
        mockBuilding = mock(Building.class);
    }

    @Test
    void testExecute_withValidBuilding() throws BuildingException {
        // Act
        addDefaultFloorCommand.execute(mockBuilding);

        // Assert
        verify(mockBuilding, times(1)).buildDefaultFloor();
    }

    @Test
    void testExecute_withBuildingException() throws BuildingException {
        // Arrange
        doThrow(new BuildingException("Cannot add default floor")).when(mockBuilding).buildDefaultFloor();

        // Act & Assert
        BuildingException exception = assertThrows(BuildingException.class, () -> addDefaultFloorCommand.execute(mockBuilding));
        assertEquals("Cannot add default floor", exception.getMessage());
    }

    @Test
    void testExecute_withNullBuilding() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> addDefaultFloorCommand.execute(null));
    }
}
