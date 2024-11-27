package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.sensors.Microphone;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MicrophoneTest {

    private Sensor microphone;
    private SecuritySystemMediator securitySystemMediator;
    private SecurityEventManager securityEventManager;

    @BeforeEach
    void setUp() {
        // Створюємо моковані об'єкти для залежностей
        securitySystemMediator = mock(SecuritySystemMediator.class);
        securityEventManager = mock(SecurityEventManager.class);

        // Створюємо об'єкт Microphone
        microphone = new Microphone();
        microphone.setMediator(securitySystemMediator);  // Встановлюємо мок для Mediator
        microphone.setEventManager(securityEventManager); // Встановлюємо мок для EventManager
    }

    @Test
    void testSetMediator() {
        // Перевіряємо, чи коректно встановлюється SecuritySystemMediator
        microphone.setMediator(securitySystemMediator);

        // Перевіряємо, чи відбулося встановлення
        assert microphone.getSecurityMediator() == securitySystemMediator;
    }

    @Test
    void testSetEventManager() {
        // Перевіряємо, чи коректно встановлюється SecurityEventManager
        microphone.setEventManager(securityEventManager);

        // Перевіряємо, чи відбулося встановлення
        assert microphone.getSecurityEventManager() == securityEventManager;
    }

    @Test
    void testDetect() throws Exception {
        // Викликаємо метод detect
        microphone.detect();

        // Перевіряємо, чи викликаються методи на securityEventManager і securityMediator
        verify(securityEventManager).securityNotify("Microphone", microphone);
        verify(securitySystemMediator).notify(microphone, "Microphone");
    }
}
