package org.example.securitysystem.service.domain_service.robber_simulator;

import lombok.Data;
import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



@Data
public class RobberSimulator {
    private final Random random;
    private final Building building;

    public RobberSimulator(Building b) {
        this.building = b;
        this.random = new Random();
    }

    public List<Sensor> getListOfAllSensors() {
        List<Sensor> sensors = new ArrayList<>();
        for (Floor floor : building.getFloors()) {
            sensors.addAll(floor.getSensors());
        }
        return sensors;
    }

    public void triggerRandomSensor() throws Exception {
        List<Sensor> sensors = getListOfAllSensors();

        if (sensors.isEmpty()) {
            throw new Exception("No sensors available to trigger.");
        }

        Sensor sensor = sensors.get(random.nextInt(sensors.size()));

        int triggerTime = random.nextInt(5000) + 1000;
        try {
            Thread.sleep(triggerTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sensor trigger interrupted", e);
        }

        sensor.detect();
    }


}