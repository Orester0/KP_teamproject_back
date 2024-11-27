package org.example.securitysystem;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.domain_service.command.AddOfficeFloorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddOfficeFloorCommandTest {

    private AddOfficeFloorCommand addOfficeFloorCommand;
    private Building mockBuilding;

    @BeforeEach
    void setUp() {
        addOfficeFloorCommand = new AddOfficeFloorCommand();
        mockBuilding = mock(Building.class);
    }

    @Test
    void testExecute_withValidBuilding() throws BuildingException {
        // Act
        addOfficeFloorCommand.execute(mockBuilding);

        // Assert
        verify(mockBuilding, times(1)).buildOfficeFloor();
    }

    @Test
    void testExecute_withBuildingException() throws BuildingException {
        // Arrange
        doThrow(new BuildingException("Cannot add office floor")).when(mockBuilding).buildOfficeFloor();

        // Act & Assert
        BuildingException exception = assertThrows(BuildingException.class, () -> addOfficeFloorCommand.execute(mockBuilding));
        assertEquals("Cannot add office floor", exception.getMessage());
    }

    @Test
    void testExecute_withNullBuilding() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> addOfficeFloorCommand.execute(null));
    }
}
