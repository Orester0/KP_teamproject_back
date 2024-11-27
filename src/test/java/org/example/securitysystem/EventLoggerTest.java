package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.service.domain_service.observer.listener.EventLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EventLoggerTest {

    private EventLogger eventLogger;
    private ISecurityColleague mockSensor;

    @BeforeEach
    void setUp() {
        eventLogger = new EventLogger();
        mockSensor = mock(ISecurityColleague.class);
        eventLogger.clearObjectList();
        eventLogger.clearObjectList2();
    }

    @Test
    void testUpdate_withRegularEventType() {
        String eventType = "SENSOR_ON";
        eventLogger.update(eventType, mockSensor);

        List<EventLogger.SensorLog> logs = eventLogger.getObjectList();
        assertEquals(1, logs.size());
        EventLogger.SensorLog log = logs.get(0);

        assertEquals(mockSensor, log.sensorDetails());
        assertTrue(log.activated());
        assertNotNull(log.currentTime());
    }

    @Test
    void testUpdate_withEventTypeEndingWithFF() {
        String eventType = "ALARM_OFF_FF";
        eventLogger.update(eventType, mockSensor);

        List<EventLogger.SensorLog> logs = eventLogger.getObjectList2();
        assertEquals(1, logs.size());
        EventLogger.SensorLog log = logs.get(0);

        assertEquals(mockSensor, log.sensorDetails());
        assertFalse(log.activated());
        assertNotNull(log.currentTime());
    }

    @Test
    void testClearObjectList() {
        eventLogger.update("SENSOR_ON", mockSensor);
        eventLogger.update("SENSOR_OFF", mockSensor);

        assertFalse(eventLogger.getObjectList().isEmpty());
        eventLogger.clearObjectList();
        assertTrue(eventLogger.getObjectList().isEmpty());
    }

    @Test
    void testClearObjectList2() {
        eventLogger.update("ALARM_OFF_FF", mockSensor);
        eventLogger.update("ALARM_ON", mockSensor);

        assertFalse(eventLogger.getObjectList2().isEmpty());
        eventLogger.clearObjectList2();
        assertTrue(eventLogger.getObjectList2().isEmpty());
    }


    @Test
    void testUpdate_withEmptyEventType() {
        String eventType = "";
        eventLogger.update(eventType, mockSensor);

        List<EventLogger.SensorLog> logs = eventLogger.getObjectList();
        assertEquals(1, logs.size());
        assertTrue(logs.get(0).activated());
    }

    @Test
    void testUpdate_withMultipleEvents() {
        eventLogger.update("SENSOR_ON", mockSensor);
        eventLogger.update("ALARM_OFF_FF", mockSensor);
        eventLogger.update("ALARM_ON", mockSensor);

        assertEquals(3, eventLogger.getObjectList().size());
        assertEquals(3, eventLogger.getObjectList2().size());
    }

    @Test
    void testUpdate_withBoundaryConditions() {
        String eventType = "A".repeat(256); // Max length string
        eventLogger.update(eventType, mockSensor);

        List<EventLogger.SensorLog> logs = eventLogger.getObjectList();
        assertEquals(1, logs.size());
        assertTrue(logs.get(0).activated());
    }
}
