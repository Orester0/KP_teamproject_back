package org.example.securitysystem;
import org.example.securitysystem.model.entity.security_system.alarms.AlarmSystem;
import org.example.securitysystem.model.entity.security_system.sensors.MotionSensor;
import org.example.securitysystem.service.domain_service.mediator.SecurityMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecuritySystemApplicationTests {

    @Mock
    private AlarmSystem alarmSystem;

    @InjectMocks
    private SecurityMediator securityController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
