package simulation;

import configuration.Configuration;
import configuration.ConfigurationReader;
import map.RectangularMap;
import visualisation.MapVisualizer;
import world.FileWriter;

import java.util.LinkedList;
import java.util.List;

public class Simulation {
    private final RectangularMap map;
    private final List<ISimulationObserver> observers;
    private final Thread simulationThread;
    private boolean isRunning;

    public Simulation() {
        this.observers = new LinkedList<>();
        String fileName="statistics.txt";
        ConfigurationReader mapConfiguration = new ConfigurationReader();
        Configuration configuration = mapConfiguration.getConfiguration();
        this.map = new RectangularMap(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getJungleWidth(), configuration.getJungleHeight(), configuration.getNumberOfPlants(), configuration.getNumberOfAnimals(), configuration.getStartEnergy(), configuration, observers);
        FileWriter writer = new FileWriter(fileName, map);
        this.simulationThread = new Thread(this::run);
    }

    public RectangularMap getMap() {
        return map;
    }

    private void makeMove() {
        map.oneDayMore();
        for (ISimulationObserver observer: observers) {
            observer.handleDayFinished();
        }
    }

    private void run() {
        while (true) {
            try {
                Thread.sleep(50);
                if (!isRunning) {
                    synchronized (simulationThread) {
                        simulationThread.wait();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            makeMove();
        }
    }

    public void startSimulation() {
        simulationThread.start();
        isRunning = true;
    }

    public void stopSimulation() {
        isRunning = false;
    }

    public void resumeSimulation() {
        synchronized (simulationThread) {
            simulationThread.notify();
        }
        isRunning = true;
    }

    public boolean getIsRunning() {
        return isRunning;
    }

    public void addObserver(MapVisualizer observer) {
        observers.add(observer);
        map.handleAddingObserver(observer);
    }
}
