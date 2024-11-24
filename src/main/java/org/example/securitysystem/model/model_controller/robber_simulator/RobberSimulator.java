package org.example.securitysystem.model.model_controller.robber_simulator;

import org.example.securitysystem.model.entity.building.Building;
import org.example.securitysystem.model.entity.building.Floor;
import org.example.securitysystem.model.entity.security_system.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RobberSimulator {
    private final Random random;
    private final Building building;
    private final List<Thread> threads = new ArrayList<>();
    private volatile boolean running = true;

    public RobberSimulator(Building b) {
        this.building = b;
        this.random = new Random();
    }

    public List<Sensor> getListOfAllSensors(){
        List<Sensor> sensors = new ArrayList<>();

        for(Floor floor : building.getFloors()){
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

    public void startSimulation(int threadCount) {
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new SensorTriggerTask());
            threads.add(thread);
        }

        for (var t : threads) {
            t.start();
        }
    }

    public void stopSimulation() {
        running = false;

        for (Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        threads.clear();
    }

    private class SensorTriggerTask implements Runnable {

        @Override
        public void run() {
            while (running) {
                try {
                    System.out.println(Thread.currentThread().getName() + " is triggering.");
                    triggerRandomSensor();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }


            }
            System.out.println(Thread.currentThread().getName() + " is stopping.");

        }
    }
}
