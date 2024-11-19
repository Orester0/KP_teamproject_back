//// FloorSimulator.java
//@Component
//public class FloorSimulator {
//    private final Random random = new Random();
//    private final SimpMessagingTemplate messagingTemplate;
//    private final ScheduledExecutorService scheduler;
//    private ScheduledFuture<?> simulationTask;
//    private final int floorNumber;
//    private final String floorType; // "DEFAULT", "OFFICE", "HOSTEL"
//    private final Long userId;
//
//    public FloorSimulator(int floorNumber, String floorType, Long userId, SimpMessagingTemplate messagingTemplate) {
//        this.floorNumber = floorNumber;
//        this.floorType = floorType;
//        this.userId = userId;
//        this.messagingTemplate = messagingTemplate;
//        this.scheduler = Executors.newSingleThreadScheduledExecutor();
//    }
//
//    public void startSimulation() {
//        if (simulationTask != null && !simulationTask.isDone()) {
//            return;
//        }
//
//        simulationTask = scheduler.scheduleAtFixedRate(
//                this::generateEvent,
//                random.nextInt(3), // Random initial delay 0-2 seconds
//                3 + random.nextInt(4), // Random interval 3-6 seconds
//                TimeUnit.SECONDS
//        );
//    }
//
//    public void stopSimulation() {
//        if (simulationTask != null) {
//            simulationTask.cancel(false);
//            scheduler.shutdown();
//        }
//    }
//
//    private void generateEvent() {
//        SensorNotification notification = switch (floorType) {
//            case "OFFICE" -> generateOfficeEvent();
//            case "HOSTEL" -> generateHostelEvent();
//            default -> generateDefaultEvent();
//        };
//
//        String topic = String.format("/topic/sensors/%d/floor/%d", userId, floorNumber);
//        messagingTemplate.convertAndSend(topic, notification);
//    }
//
//    private SensorNotification generateOfficeEvent() {
//        String[] sensorTypes = {"MOTION", "TEMPERATURE", "SMOKE", "DOOR", "WORKSTATION"};
//        return generateEventByTypes(sensorTypes);
//    }
//
//    private SensorNotification generateHostelEvent() {
//        String[] sensorTypes = {"MOTION", "TEMPERATURE", "SMOKE", "DOOR", "WATER_LEAK"};
//        return generateEventByTypes(sensorTypes);
//    }
//
//    private SensorNotification generateDefaultEvent() {
//        String[] sensorTypes = {"MOTION", "TEMPERATURE", "SMOKE", "DOOR"};
//        return generateEventByTypes(sensorTypes);
//    }
//
//    private SensorNotification generateEventByTypes(String[] sensorTypes) {
//        String sensorType = sensorTypes[random.nextInt(sensorTypes.length)];
//        String status = generateStatus(sensorType);
//
//        return new SensorNotification(
//                generateSensorId(sensorType),
//                floorNumber,
//                sensorType,
//                status,
//                generateMessage(sensorType, status)
//        );
//    }
//
//    private Long generateSensorId(String sensorType) {
//        // Create consistent sensor IDs for each floor and type
//        return Long.parseLong(String.format("%d%d%d",
//                floorNumber,
//                sensorType.hashCode() % 100,
//                random.nextInt(10)));
//    }
//
//    private String generateStatus(String sensorType) {
//        int randomValue = random.nextInt(100);
//        return switch (sensorType) {
//            case "SMOKE" -> randomValue < 90 ? "NORMAL" : "ALERT";
//            case "TEMPERATURE" -> randomValue < 80 ? "NORMAL" :
//                    randomValue < 95 ? "WARNING" : "ALERT";
//            default -> randomValue < 85 ? "NORMAL" : "WARNING";
//        };
//    }
//
//    private String generateMessage(String sensorType, String status) {
//        return switch (sensorType) {
//            case "MOTION" -> switch (status) {
//                case "NORMAL" -> "No movement detected";
//                case "WARNING" -> "Movement detected in restricted area";
//                default -> "Multiple movements detected";
//            };
//            case "WORKSTATION" -> switch (status) {
//                case "NORMAL" -> "Workstation occupied";
//                case "WARNING" -> "Workstation inactive for extended period";
//                default -> "Unauthorized workstation access";
//            };
//            case "WATER_LEAK" -> switch (status) {
//                case "NORMAL" -> "No water leaks detected";
//                case "WARNING" -> "Moisture level increased";
//                default -> "Water leak detected!";
//            };
//            // Add other sensor types...
//            default -> String.format("%s sensor status: %s", sensorType, status);
//        };
//    }
//}
//
//// BuildingSimulationManager.java
//@Service
//public class BuildingSimulationManager {
//    private final SimpMessagingTemplate messagingTemplate;
//    private final Map<Long, List<FloorSimulator>> buildingSimulations = new ConcurrentHashMap<>();
//
//    @Autowired
//    public BuildingSimulationManager(SimpMessagingTemplate messagingTemplate) {
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    public List<String> startSimulation(Long userId, Building building) {
//        stopSimulation(userId); // Stop existing simulation if any
//
//        List<FloorSimulator> floorSimulators = new ArrayList<>();
//        List<String> topics = new ArrayList<>();
//
//        for (int i = 0; i < building.getFloors().size(); i++) {
//            Floor floor = building.getFloors().get(i);
//            String floorType = determineFloorType(floor);
//
//            FloorSimulator simulator = new FloorSimulator(
//                    i,
//                    floorType,
//                    userId,
//                    messagingTemplate
//            );
//
//            simulator.startSimulation();
//            floorSimulators.add(simulator);
//
//            String topic = String.format("/topic/sensors/%d/floor/%d", userId, i);
//            topics.add(topic);
//        }
//
//        buildingSimulations.put(userId, floorSimulators);
//        return topics;
//    }
//
//    private String determineFloorType(Floor floor) {
//        // Determine floor type based on your Floor class implementation
//        if (floor instanceof OfficeFloor) return "OFFICE";
//        if (floor instanceof HostelFloor) return "HOSTEL";
//        return "DEFAULT";
//    }
//
//    public void stopSimulation(Long userId) {
//        List<FloorSimulator> simulators = buildingSimulations.remove(userId);
//        if (simulators != null) {
//            simulators.forEach(FloorSimulator::stopSimulation);
//        }
//    }
//}
//
//// Modified BuildingController endpoint
//@PostMapping("/startSimulation")
//public ResponseEntity<SimulationResponse> startSimulation(@RequestParam Long userId) {
//    try {
//        var user = userService.getUser(userId);
//        Building building = getUserBuilding(user);
//
//        if (!building.isFinalized()) {
//            return ResponseEntity.badRequest()
//                    .body(new SimulationResponse(null, "Building must be finalized before starting simulation"));
//        }
//
//        List<String> topics = simulationManager.startSimulation(userId, building);
//
//        return ResponseEntity.ok(new SimulationResponse(topics, "Simulation started successfully"));
//    } catch (Exception e) {
//        return ResponseEntity.internalServerError()
//                .body(new SimulationResponse(null, "Error: " + e.getMessage()));
//    }
//}
//
//// Updated SimulationResponse
//public class SimulationResponse {
//    private List<String> socketTopics;
//    private String message;
//
//    public SimulationResponse(List<String> socketTopics, String message) {
//        this.socketTopics = socketTopics;
//        this.message = message;
//    }
//
//    // getters
//}