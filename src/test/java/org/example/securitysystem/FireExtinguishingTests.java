package org.example.securitysystem;

import org.example.securitysystem.model.entity.security_system.alarms.FireExtinguishing;
import org.example.securitysystem.service.domain_service.observer.SecurityEventManager;
import org.example.securitysystem.service.domain_service.mediator.SecuritySystemMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FireExtinguishingTests {

    private FireExtinguishing fireExtinguishing;
    private SecurityEventManager securityEventManagerMock;
    private SecuritySystemMediator securitySystemMediatorMock;

    @BeforeEach
    void setUp() {
        // Створюємо mock для securityEventManager та securitySystemMediator
        securityEventManagerMock = mock(SecurityEventManager.class);
        securitySystemMediatorMock = mock(SecuritySystemMediator.class);

        // Створюємо об'єкт FireExtinguishing
        fireExtinguishing = new FireExtinguishing();

        // Налаштовуємо залежності через наявні методи
        fireExtinguishing.setEventManager(securityEventManagerMock);  // Використовуємо наявний метод
        fireExtinguishing.setMediator(securitySystemMediatorMock);     // Використовуємо наявний метод
    }

    // Тест для активації сигналу тривоги
    @Test
    void testActivateAlarm() throws Exception {
        fireExtinguishing.activateAlarm();

        // Перевіряємо, що при активації надіслано сповіщення про включення системи
        verify(securityEventManagerMock).securityNotify("FireExtinguishingON", fireExtinguishing);

        // Перевірка, що сигнал активований
        assertTrue(fireExtinguishing.isAlarmActive(), "Сигнал тривоги не активовано.");
    }

    // Тест для деактивації сигналу тривоги
    @Test
    void testDeactivateAlarm() throws Exception {
        fireExtinguishing.activateAlarm();  // Спочатку активуємо сигнал
        fireExtinguishing.deactivateAlarm();

        // Перевіряємо, що при деактивації надіслано сповіщення про вимкнення системи
        verify(securityEventManagerMock).securityNotify("FireExtinguishingOFF", fireExtinguishing);

        // Перевіряємо, що сигнал деактивований
        assertFalse(fireExtinguishing.isAlarmActive(), "Сигнал тривоги не був вимкнений.");
    }

    // Тест для коректної обробки виключень (якщо активувати або деактивувати сигнал не вдалося)
    @Test
    void testActivateAlarm_exceptionHandling() {
        try {
            fireExtinguishing.activateAlarm();
            fail("Очікувалося виключення через помилку в активації сигналу.");
        } catch (Exception e) {
            assertEquals("Thread interrupted", e.getMessage(), "Невірне повідомлення про помилку.");
        }
    }
}
