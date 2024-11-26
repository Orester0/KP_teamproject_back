//package org.example.securitysystem;
//
//import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
//import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
//import org.example.securitysystem.model.model_controller.mediator.SecurityMediator;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//class SecuritySystemApplicationTests {
//
//    @Mock
//    private AlarmSystem alarmSystem;
//
//    @InjectMocks
//    private SecurityMediator securityController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testMotionDetectionTriggersAlarm() throws Exception {
//        // Створюємо новий сенсор
//        MotionSensor motionSensor = new MotionSensor();
//        motionSensor.setMediator(securityController);
//
//        // Реєструємо компоненти
//        securityController.register(motionSensor, "MotionSensor");
//        securityController.register(alarmSystem, "AlarmSystem");
//
//        // Імітуємо виявлення руху
//        motionSensor.detect();
//
//        // Перевіряємо, чи був викликаний метод активації
//        verify(alarmSystem, times(1)).activateAlarm();
//    }
//}
