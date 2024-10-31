

# Security System Simulation

This repository contains a Java project for simulating a security system within a building environment. 
The system models different sensors, rooms, and alerts to handle various security events. 
The architecture uses the mediator and observer design patterns to efficiently manage communication between components.

## Project Structure

### Main Components
1. **Mediator**: Manages communication between various security components.
2. **Observer**: Listens to and reacts to security events.
3. **Rooms & Sensors**: Defines different rooms equipped with sensors (motion, temperature, camera, and microphone).
4. **Floor Builder**: Constructs rooms and organizes them into floors and buildings.
5. **Controller**: Manages user interactions and requests for event handling.
6. **Database**: Stores event logs and sensor statuses.

### Diagram
The architecture diagram (attached in this repository) provides a visual overview of the components and their relationships within the security simulation. The main modules are interconnected to handle real-time updates, sensor activations, and event notifications.

## Components

### Exceptions
- **Exceptions**: All the exceptions contained in object named 'zxc'.

### Mediator
- **SecuritySystemMediator**: Central mediator that manages sensor activations and communication.
- **SecurityController**: Oversees event notifications and registration of security colleagues.
- **AlarmSystem**: Sends alerts when a security breach is detected.

### Observer
- **SecurityEventManager**: Tracks and updates event listeners.
- **EventLogger**: Logs events into the database.
- **Notifier**: Sends notifications about security events.

### Rooms and Sensors
- **Room**: Represents various rooms (e.g., Hall, Office, Kitchen) and calculates the sensor requirements.
- **Sensor**: Abstract sensor class with specific implementations, such as `MotionSensor`, `TemperatureSensor`, `Camera`, and `Microphone`.

### Floor and Building
- **FloorBuilder**: Constructs floors with specified rooms.
- **Building**: Manages floors and keeps track of all rooms and sensors.

### Controller
- **Controller**: Manages simulation controls, such as activating sensors and triggering alarm events.

## Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/Orester0/security-system.git
    cd security-system
    ```

2. **Import the Project**:
   Open the project in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse).

3. **Install Dependencies**:
   Ensure you have Java SDK 8 or higher installed. You may also need to set up additional libraries for database connectivity if used (e.g., JDBC).

4. **Build the Project**:
   Use your IDE’s build tools or build manually with Maven/Gradle if this project uses a build system.

5. **Run the Simulation**:
   Run the `Main` class to start the simulation.

## Usage

- **Frontend**: The frontend interface connects with the `Controller` class to visualize sensor activations and floor layouts.
- **Robber Simulation**: The `RobberSimulator` class randomly activates sensors to test the system's response.


![Пацєтко](https://static26.tgcnt.ru/posts/_0/03/03c49d854573fcb43320964f39b7e047.jpg)
