package org.example.securitysystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.model_controller.mediator.SecurityController;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import static org.mockito.Mockito.*;

@SpringBootTest
class SecuritySystemApplicationTests {

    @Mock
    private AlarmSystem alarmSystem;

    @InjectMocks
    private SecurityController securityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityController = new SecurityController();
    }

    @Test
    void contextLoads() {

    }

    @Test
    void testMotionDetectionTriggersAlarm() throws Exception {
        MotionSensor motionSensor = new MotionSensor();
        motionSensor.setMediator(securityController);

        securityController.register(motionSensor, "MotionSensor");
        securityController.register(alarmSystem, "AlarmSystem");

        motionSensor.detect();

        verify(alarmSystem, times(1)).activateAlarm();
    }
}
