package org.example.securitysystem;

import org.example.securitysystem.exception.BuildingException;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.service.domain_service.command.AddHostelFloorCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddHostelFloorCommandTest {

    private AddHostelFloorCommand addHostelFloorCommand;
    private Building mockBuilding;

    @BeforeEach
    void setUp() {
        addHostelFloorCommand = new AddHostelFloorCommand();
        mockBuilding = mock(Building.class);
    }

    @Test
    void testExecute_withValidBuilding() throws BuildingException {
        // Act
        addHostelFloorCommand.execute(mockBuilding);

        // Assert
        verify(mockBuilding, times(1)).buildHostelFloor();
    }

    @Test
    void testExecute_withBuildingException() throws BuildingException {
        // Arrange
        doThrow(new BuildingException("Cannot add hostel floor")).when(mockBuilding).buildHostelFloor();

        // Act & Assert
        BuildingException exception = assertThrows(BuildingException.class, () -> addHostelFloorCommand.execute(mockBuilding));
        assertEquals("Cannot add hostel floor", exception.getMessage());
    }

    @Test
    void testExecute_withNullBuilding() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> addHostelFloorCommand.execute(null));
    }
}
