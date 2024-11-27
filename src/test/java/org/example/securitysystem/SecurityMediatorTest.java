package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.ISecurityColleague;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.service.domain_service.mediator.SecurityMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityMediatorTest {

    private SecurityMediator securityMediator;
    private ISecurityColleague mockSensor;
    private AlarmSystem mockSiren;
    private AlarmSystem mockSpeaker;
    private AlarmSystem mockFireExtinguisher;

    @BeforeEach
    void setUp() {
        securityMediator = new SecurityMediator();
        mockSensor = mock(ISecurityColleague.class);
        mockSiren = mock(AlarmSystem.class);
        mockSpeaker = mock(AlarmSystem.class);
        mockFireExtinguisher = mock(AlarmSystem.class);
    }

    @Test
    void testRegisterAndNotify_withMotionSensorEvent() throws Exception {
        securityMediator.register(mockSiren, "Siren");
        securityMediator.register(mockSensor, "MotionSensor");

        securityMediator.notify(mockSensor, "MotionSensor");

        verify(mockSiren, times(1)).activateAlarm();
    }

    @Test
    void testRegisterAndNotify_withMicrophoneEvent() throws Exception {
        securityMediator.register(mockSpeaker, "Speakers");
        securityMediator.register(mockSensor, "Microphone");

        securityMediator.notify(mockSensor, "Microphone");

        verify(mockSpeaker, times(1)).activateAlarm();
    }

    @Test
    void testRegisterAndNotify_withTemperatureSensorEvent() throws Exception {
        securityMediator.register(mockFireExtinguisher, "FireExtinguishing");
        securityMediator.register(mockSensor, "TemperatureSensor");

        securityMediator.notify(mockSensor, "TemperatureSensor");

        verify(mockFireExtinguisher, times(1)).activateAlarm();
    }

    @Test
    void testNotify_withUnknownEvent() {
        Exception exception = assertThrows(Exception.class, () -> {
            securityMediator.notify(mockSensor, "UnknownEvent");
        });

        assertEquals("Unknown Event", exception.getMessage());
    }

    @Test
    void testRegister_multipleColleaguesSameType() throws Exception {
        ISecurityColleague mockSiren2 = mock(AlarmSystem.class);

        securityMediator.register(mockSiren, "Siren");
        securityMediator.register(mockSiren2, "Siren");

        securityMediator.notify(mockSensor, "MotionSensor");

        verify(mockSiren, times(1)).activateAlarm();
    }





    @Test
    void testNotify_withNullEvent() {
        assertThrows(NullPointerException.class, () -> securityMediator.notify(mockSensor, null));
    }


}
