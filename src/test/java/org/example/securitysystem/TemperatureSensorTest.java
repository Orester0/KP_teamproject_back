package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.example.securitysystem.service.domain_service.mediator.SecurityMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class TemperatureSensorTest {

    private TemperatureSensor temperatureSensor;
    private SecurityEventManager securityEventManager;
    private SecurityMediator securityMediator;

    @BeforeEach
    void setUp() {
        // Створюємо моковані об'єкти для залежностей
        securityEventManager = mock(SecurityEventManager.class);
        securityMediator = mock(SecurityMediator.class);

        // Створюємо TemperatureSensor і ін'єктуємо моковані залежності
        temperatureSensor = new TemperatureSensor();

        // Використовуємо рефлексію або іншою ін'єкцією залежностей
        // Для тесту передаємо моки без сеттерів (якщо вони є)
        temperatureSensor.setSecurityEventManager(securityEventManager); // Це приклад, якщо вони є приватними змінними
        temperatureSensor.setSecurityMediator(securityMediator); // Аналогічно
    }

    @Test
    void testDetectHighTemperatureEvent() throws Exception {
        // Викликаємо метод detect, щоб перевірити взаємодію з іншими об'єктами
        temperatureSensor.detect();

        // Перевіряємо, чи викликаються методи на securityEventManager і securityMediator
        verify(securityEventManager).securityNotify("TemperatureSensor", temperatureSensor);
        verify(securityMediator).notify(temperatureSensor, "TemperatureSensor");
    }

    @Test
    void testDetectWithMockedDependencies() throws Exception {
        // Викликаємо метод detect
        temperatureSensor.detect();

        // Перевіряємо правильність взаємодії з securityEventManager та securityMediator
        verify(securityEventManager, times(1)).securityNotify("TemperatureSensor", temperatureSensor);
        verify(securityMediator, times(1)).notify(temperatureSensor, "TemperatureSensor");
    }
}
