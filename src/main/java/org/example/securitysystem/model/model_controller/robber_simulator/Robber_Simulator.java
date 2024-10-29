package org.example.securitysystem.model.model_controller.robber_simulator;

import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.room.Room;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Robber_Simulator {
    private Random random;
    List<Floor> floors;

    public Robber_Simulator(List<Floor> floors) {
        this.floors = floors;
        this.random = new Random();
    }

    public List<Sensor> getListOfAllSensors(){
        List<Sensor> sensors = new ArrayList<>();

        for(Floor floor : floors){
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
            e.printStackTrace();
        }

        sensor.detect();
    }

    public void startSimulation(int triggerCount) {
        for (int i = 0; i < triggerCount; i++) {
            try{
                triggerRandomSensor();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
