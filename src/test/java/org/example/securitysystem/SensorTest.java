package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.model.entity.security_system.sensors.TemperatureSensor;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class SensorTest {

    private Sensor sensor;
    private SecuritySystemMediator securitySystemMediator;
    private SecurityEventManager securityEventManager;

    @BeforeEach
    void setUp() {
        // Створюємо моковані об'єкти для залежностей
        securitySystemMediator = mock(SecuritySystemMediator.class);
        securityEventManager = mock(SecurityEventManager.class);

        // Створюємо об'єкт Sensor (у реальному коді потрібно буде мати конкретну реалізацію, бо Sensor є абстрактним)
        sensor = new TemperatureSensor(); // Використовуємо TemperatureSensor як приклад
        sensor.setMediator(securitySystemMediator);  // Встановлюємо мок для Mediator
        sensor.setEventManager(securityEventManager); // Встановлюємо мок для EventManager
    }

    @Test
    void testSetMediator() {
        // Перевіряємо, чи коректно встановлюється SecuritySystemMediator
        sensor.setMediator(securitySystemMediator);

        // Перевіряємо, чи відбулося встановлення
        assert sensor.getSecurityMediator() == securitySystemMediator;
    }

    @Test
    void testSetEventManager() {
        // Перевіряємо, чи коректно встановлюється SecurityEventManager
        sensor.setEventManager(securityEventManager);

        // Перевіряємо, чи відбулося встановлення
        assert sensor.getSecurityEventManager() == securityEventManager;
    }





}
