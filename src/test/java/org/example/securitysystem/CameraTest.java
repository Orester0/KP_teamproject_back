package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.sensors.Camera;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CameraTest {

    private Sensor camera;
    private SecuritySystemMediator securitySystemMediator;
    private SecurityEventManager securityEventManager;

    @BeforeEach
    void setUp() {
        // Створюємо моковані об'єкти для залежностей
        securitySystemMediator = mock(SecuritySystemMediator.class);
        securityEventManager = mock(SecurityEventManager.class);

        // Створюємо об'єкт Camera
        camera = new Camera();
        camera.setMediator(securitySystemMediator);  // Встановлюємо мок для Mediator
        camera.setEventManager(securityEventManager); // Встановлюємо мок для EventManager
    }

    @Test
    void testSetMediator() {
        // Перевіряємо, чи коректно встановлюється SecuritySystemMediator
        camera.setMediator(securitySystemMediator);

        // Перевіряємо, чи відбулося встановлення
        assert camera.getSecurityMediator() == securitySystemMediator;
    }

    @Test
    void testSetEventManager() {
        // Перевіряємо, чи коректно встановлюється SecurityEventManager
        camera.setEventManager(securityEventManager);

        // Перевіряємо, чи відбулося встановлення
        assert camera.getSecurityEventManager() == securityEventManager;
    }

    @Test
    void testDetect() throws Exception {
        // Викликаємо метод detect
        camera.detect();

        // Перевіряємо, чи викликаються методи на securityEventManager і securityMediator
        verify(securityEventManager).securityNotify("Camera", camera);
        verify(securitySystemMediator).notify(camera, "Camera");
    }
}
