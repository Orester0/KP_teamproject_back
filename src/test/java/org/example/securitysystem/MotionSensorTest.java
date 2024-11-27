package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class MotionSensorTest {

    private Sensor motionSensor;
    private SecuritySystemMediator securitySystemMediator;
    private SecurityEventManager securityEventManager;

    @BeforeEach
    void setUp() {
        // Створюємо моковані об'єкти для залежностей
        securitySystemMediator = mock(SecuritySystemMediator.class);
        securityEventManager = mock(SecurityEventManager.class);

        // Створюємо об'єкт MotionSensor
        motionSensor = new MotionSensor();
        motionSensor.setMediator(securitySystemMediator);  // Встановлюємо мок для Mediator
        motionSensor.setEventManager(securityEventManager); // Встановлюємо мок для EventManager
    }

    @Test
    void testSetMediator() {
        // Перевіряємо, чи коректно встановлюється SecuritySystemMediator
        motionSensor.setMediator(securitySystemMediator);

        // Перевіряємо, чи відбулося встановлення
        assert motionSensor.getSecurityMediator() == securitySystemMediator;
    }

    @Test
    void testSetEventManager() {
        // Перевіряємо, чи коректно встановлюється SecurityEventManager
        motionSensor.setEventManager(securityEventManager);

        // Перевіряємо, чи відбулося встановлення
        assert motionSensor.getSecurityEventManager() == securityEventManager;
    }

    @Test
    void testDetect() throws Exception {
        // Викликаємо метод detect
        motionSensor.detect();

        // Перевіряємо, чи викликаються методи на securityEventManager і securityMediator
        verify(securityEventManager).securityNotify("MotionSensor", motionSensor);
        verify(securitySystemMediator).notify(motionSensor, "MotionSensor");
    }
}
