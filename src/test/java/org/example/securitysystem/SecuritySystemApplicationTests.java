package org.example.securitysystem;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecuritySystemApplicationTests {

//    @Mock
//    private AlarmSystem alarmSystem;
//
//    @InjectMocks
//    private SecurityController securityController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        securityController = new SecurityController();
//    }
//
//    @Test
//    void contextLoads() {
//
//    }
//
//    @Test
//    void testMotionDetectionTriggersAlarm() throws Exception {
//        MotionSensor motionSensor = new MotionSensor();
//        motionSensor.setMediator(securityController);
//
//        securityController.register(motionSensor, "MotionSensor");
//        securityController.register(alarmSystem, "AlarmSystem");
//
//        motionSensor.detect();
//
//        verify(alarmSystem, times(1)).activateAlarm();
//    }
}
