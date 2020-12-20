package configuration;

public class Configuration {
    private final int mapWidth;
    private final int mapHeight;
    private final int jungleWidth;
    private final int jungleHeight;
    private final int numberOfPlants;
    private final int numberOfAnimals;
    private final int plantEnergy;
    private final int minEnergyForReproduction;
    private final int liveEnergy;
    private final int startEnergy;

    public Configuration() {
        mapWidth = 100;
        mapHeight = 100;
        jungleWidth = 15;
        jungleHeight = 15;
        numberOfPlants = 2000;
        numberOfAnimals = 60;
        plantEnergy = 24;
        startEnergy = 48;
        minEnergyForReproduction = startEnergy / 2;
        liveEnergy = 1;
    }

    public Configuration(int mapWidth, int mapHeight, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio, int numberOfAnimals, int numberOfPlants) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        jungleWidth = (int) (mapWidth * jungleRatio);
        jungleHeight = (int) (mapHeight * jungleRatio);
        this.numberOfPlants = numberOfPlants;
        this.numberOfAnimals = numberOfAnimals;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
        minEnergyForReproduction = startEnergy / 2;
        liveEnergy = moveEnergy;

    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getJungleWidth() {
        return jungleWidth;
    }

    public int getJungleHeight() {
        return jungleHeight;
    }

    public int getLiveEnergy() {
        return liveEnergy;
    }

    public int getMinEnergyForReproduction() {
        return minEnergyForReproduction;
    }

    public int getNumberOfAnimals() {
        return numberOfAnimals;
    }

    public int getNumberOfPlants() {
        return numberOfPlants;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getStartEnergy() {
        return startEnergy;
    }
}
