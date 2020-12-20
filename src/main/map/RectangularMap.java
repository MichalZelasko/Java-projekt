package map;

import java.util.*;

import static java.lang.Math.abs;
import static java.lang.System.exit;

import simulation.ISimulationObserver;
import statisticsObservers.ActualStatistics;
import statisticsObservers.AverageStatistics;
import worldMapElement.*;
import configuration.Configuration;

public class RectangularMap {
    private final int mapWidth;
    private final int mapHeight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    protected List<Animal> animals = new ArrayList<>();
    protected List<Plant> plants = new ArrayList<>();
    private SingularField[][] fieldsArray;
    private int junglePlantNumber = 0;
    private int plantNumber = 0;
    private int numberOfAnimals = 0;
    private int startEnergy = 0;
    ActualStatistics mapStatistics;
    AverageStatistics mapAverageStatistics;
    private List<ISimulationObserver> observers;
    private int time;
    int status;
    private Configuration configuration;

    public RectangularMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight, int numberOfPlants, int numberOfAnimals, int startEnergy, Configuration configuration, List<ISimulationObserver> observers) {
        this.observers = observers;
        this.configuration = configuration;
        time = 0;
        status = 1;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.jungleLowerLeft = new Vector2d((mapWidth - jungleWidth) / 2 + 1, (mapHeight - jungleHeight) / 2 + 1);
        this.jungleUpperRight = new Vector2d((mapWidth - jungleWidth) / 2 + jungleWidth - 1, (mapHeight - jungleHeight) / 2 + jungleHeight - 1);
        mapStatistics = new ActualStatistics(numberOfAnimals, startEnergy, this);
        mapAverageStatistics = new AverageStatistics(this);
        fieldsArray = new SingularField[mapWidth][mapHeight];
        this.plantNumber = numberOfPlants;
        this.numberOfAnimals = numberOfAnimals;
        this.startEnergy = startEnergy;
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                fieldsArray[i][j] = new SingularField(new Vector2d(i, j), this);
            }
        }
    }

    public void initializeLife() {
        Random generator = new Random();
        for (int i = 0; i < this.plantNumber; i++) {
            if (generator.nextInt() % 2 == 0) {
                addPlantInsideJungle();
            } else {
                addPlantOutsideJungle();
            }
        }
        for (int i = 0; i < this.numberOfAnimals; i++) {
            addAnimal(startEnergy);
        }
    }

    public List<ISimulationObserver> getObservers() {
        return this.observers;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public double getAverageLongevity() {
        return mapStatistics.getAverageLongevity();
    }

    public double getAverageEnergy() {
        return mapStatistics.getAverageEnergy();
    }

    public double getAverageNumberOfChildren() {
        return mapStatistics.getAverageNumberOfChildren();
    }

    public void removeDeadAnimals() {
        for (Animal iterator : animals) {
            if (iterator.getEnergy() <= 0) {
                iterator.die();
                mapStatistics.actualizeStatisticsDead(iterator);
            }
        }
        animals.removeIf(a1 -> a1.getEnergy() <= 0);
    }

    public void moveAnimals() {
        if (animals.isEmpty()) {
            System.out.println("All animals extincted.");
            exit(1);
        }
        for (Animal iterator : animals) {
            iterator.move(mapWidth, mapHeight);
            fieldsArray[iterator.getPosition().getX()][iterator.getPosition().getY()].addAnimal(iterator);
        }
    }

    public void live(int liveEnergy) {
        mapStatistics.actualizeStatisticsLife(liveEnergy);
        mapAverageStatistics.actualizeStatistics();
        for (Animal iterator : animals) {
            iterator.live(liveEnergy);
        }
    }

    public void cleanUpFields() {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                fieldsArray[i][j].cleanUpField();
            }
        }
    }

    public void consumption(int plantEnergy) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                mapStatistics.actualizeStatisticsConsumption(fieldsArray[i][j].eatPlant(plantEnergy));
            }
        }
    }

    public void reproduction(int minEnergy) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                Animal newAnimal = fieldsArray[i][j].reproduction(minEnergy);
                if (newAnimal != null) {
                    mapStatistics.actualizeStatisticsBirth();
                    putAnimal(newAnimal);
                    fieldsArray[newAnimal.getPosition().getX()][newAnimal.getPosition().getY()].addAnimal(newAnimal);
                }
            }
        }
    }

    public void addAnimal(int startEnergy) {
        Random generator = new Random();
        int[] counterArray = new int[32];
        for (int i = 0; i < 32; i++) {
            counterArray[abs(generator.nextInt()) % 8]++;
        }
        byte[] genotype = new byte[32];
        int j = 0;
        for (int i = 0; i < 8; i++) {
            while (counterArray[i] > 0) {
                genotype[j] = (byte) i;
                j++;
                counterArray[i]--;
            }
        }
        Genotype genotype1 = new Genotype(genotype);
        Animal a = new Animal(this, new Vector2d(abs(generator.nextInt()) % mapWidth, abs(generator.nextInt()) % mapHeight), genotype1, startEnergy);
        this.putAnimal(a);
    }

    public void putAnimal(Animal a) {
        mapStatistics.actualizeStatisticsNewAnimal(a);
        mapAverageStatistics.actualizeGenotypeStatistics(a);
        animals.add(a);
    }

    private int getJungleSurface() {
        return (jungleLowerLeft.subtract(jungleUpperRight).getX() - 1) * (jungleLowerLeft.subtract(jungleUpperRight).getY() - 1);
    }

    private int getSurface() {
        return mapHeight * mapWidth;
    }

    public void addPlantOutsideJungle() {
        int numberOfFreeFields = getSurface() - getJungleSurface() - plantNumber + junglePlantNumber;
        if (numberOfFreeFields <= 0) {
            return;
        }
        Random generator = new Random();
        int numberOfField = abs(generator.nextInt()) % numberOfFreeFields;
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                if (!isJunglePosition(new Vector2d(i, j)) && !fieldsArray[i][j].getPlantPresence()) {
                    numberOfField--;
                    if (numberOfField == 0) {
                        fieldsArray[i][j].putPlant();
                        plantNumber++;
                        return;
                    }
                }
            }
        }
    }

    public void addPlantInsideJungle() {
        int numberOfFreeFields = getJungleSurface() - junglePlantNumber;
        if (numberOfFreeFields <= 0) {
            return;
        }
        Random generator = new Random();
        int numberOfField = abs(generator.nextInt()) % numberOfFreeFields;
        for (int i = jungleLowerLeft.getX(); i <= jungleUpperRight.getX(); i++) {
            for (int j = jungleLowerLeft.getY(); j <= jungleUpperRight.getY(); j++) {
                if (!fieldsArray[i][j].getPlantPresence()) {
                    numberOfField--;
                    if (numberOfField <= 0) {
                        fieldsArray[i][j].putPlant();
                        junglePlantNumber++;
                        plantNumber++;
                        return;
                    }
                }
            }
        }
    }

    public void addNewPlants() {
        addPlantOutsideJungle();
        addPlantInsideJungle();
    }

    public void removePlantInsideJungle(Vector2d plantPosition) {
        junglePlantNumber--;
        removePlant(plantPosition);
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void removePlant(Vector2d plantPosition) {
        plants.removeIf(p -> p.getPosition().equals(plantPosition));
        plantNumber--;
    }

    public Object objectAt(Vector2d v1) {
        return fieldsArray[v1.getX()][v1.getY()].objectAt();
    }

    protected boolean isJunglePosition(Vector2d v) {
        return v.precedes(jungleUpperRight) && v.follows(jungleLowerLeft);
    }

    public int getPlantNumber() {
        return plantNumber;
    }

    public int getJunglePlantNumber() {
        return junglePlantNumber;
    }

    public int getNumberOfAnimals() {
        return animals.size();
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public Vector2d getJungleLowerLeft() {
        return jungleLowerLeft;
    }

    public Vector2d getJungleUpperRight() {
        return jungleUpperRight;
    }

    public byte[] getAverageGenotype() {
        return mapStatistics.getAverageGenotype();
    }

    public byte[] getTotalMostPopularGenotype() {
        return mapAverageStatistics.getMostPopularGenotype();
    }

    public double getAverageAverageEnergy() {
        return mapAverageStatistics.getAverageEnergy();
    }

    public double getAverageAverageNumberOfChildren() {
        return mapAverageStatistics.getAverageNumberOfChildren();
    }

    public double getAverageAverageLongevity() {
        return mapAverageStatistics.getAverageLongevity();
    }

    public double getAverageNumberOfAnimals() {
        return mapAverageStatistics.getAverageNumberOfAnimals();
    }

    public double getAverageNumberOfPlant() {
        return mapAverageStatistics.getAverageNumberOfPlants();
    }

    public double getAverageNumberOfPlantInJungle() {
        return mapAverageStatistics.getAverageNumberOfPlantsInJungle();
    }

    public byte[] getMostPopularGenotype() {
        return mapStatistics.getMostPopularGenotype();
    }

    public void oneDayMore() {
        if(time == 0){
            initializeLife();
        }
        status = time + 1;
        if(status > 0) {
            removeDeadAnimals();
            consumption(configuration.getPlantEnergy());
            reproduction(configuration.getMinEnergyForReproduction());
            live(configuration.getLiveEnergy());
            cleanUpFields();
            moveAnimals();
            addNewPlants();
            //printActualStatistics();
            //printAverageStatistics();
            time++;
        }
    }

    public void printActualStatistics() {
        System.out.println(time);
        System.out.print(getJunglePlantNumber() + " " + getPlantNumber() + " " + getNumberOfAnimals() + " ");
        System.out.format("%.2f ", getAverageLongevity());
        System.out.format("%.2f ", getAverageEnergy());
        System.out.format("%.2f\n", getAverageNumberOfChildren());
        System.out.println(Arrays.toString(getTotalMostPopularGenotype()));
        System.out.println();
    }

    public void printAverageStatistics() {
        System.out.println(time);
        System.out.print(getAverageNumberOfPlant() + " " + getAverageNumberOfPlantInJungle() + " " + getAverageNumberOfAnimals() + " ");
        System.out.format("%.2f ", getAverageAverageLongevity());
        System.out.format("%.2f ", getAverageAverageEnergy());
        System.out.format("%.2f\n", getAverageAverageNumberOfChildren());
        System.out.println(Arrays.toString(getMostPopularGenotype()));
        System.out.println(Arrays.toString(getAverageGenotype()));
        System.out.println();
    }

    public int getActualTime() {
        return time;
    }

    public void changeStatus(int signal) {
        status = signal;
    }

    public void handleAddingObserver(ISimulationObserver observer) {
        for(Plant iterator : plants) {
            iterator.addObserver(observer);
        }
        for(Animal iterator : animals) {
            iterator.addObserver(observer);
        }
    }
}
