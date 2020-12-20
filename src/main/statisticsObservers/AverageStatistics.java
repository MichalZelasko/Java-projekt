package statisticsObservers;

import worldMapElement.Genotype;

import java.util.HashMap;
import java.util.Map;
import map.RectangularMap;
import worldMapElement.Animal;

public class AverageStatistics implements IStatisticsObserver{
    public double averageNumberOfPlants;
    public double averageNumberOfPlantsInJungle;
    public double averageNumberOfAnimals;
    public double averageEnergy;
    public double averageNumberOfChildren;
    public double averageLongevity;
    private int time = 0;
    private HashMap<Genotype, Integer> genotypeMap;
    private RectangularMap map;

    public AverageStatistics(RectangularMap map) {
        this.map = map;
        genotypeMap = new HashMap<>();
        averageEnergy = 0.0;
        averageNumberOfChildren = 0.0;
        averageNumberOfAnimals = 0.0;
        averageNumberOfPlantsInJungle = 0.0;
        averageNumberOfPlants = 0.0;
        averageLongevity = 0.0;
    }

    public void actualizeStatistics() {
        averageNumberOfPlants = (averageNumberOfPlants * time + map.getPlantNumber()) / (time + 1);
        averageNumberOfPlantsInJungle = (averageNumberOfPlantsInJungle * time + map.getJunglePlantNumber()) / (time + 1);
        averageEnergy = (averageEnergy * time + map.getAverageEnergy()) / (time + 1);
        averageNumberOfAnimals = (averageNumberOfAnimals * time + map.getNumberOfAnimals()) / (time + 1);
        averageNumberOfChildren = (averageNumberOfChildren * time + map.getAverageNumberOfChildren()) / (time + 1);
        averageLongevity = (averageEnergy * time + map.getAverageLongevity()) / (time + 1);
        time++;
    }

    public void actualizeGenotypeStatistics(Animal animal) {
        addGenotype(animal.getGenotypeGeneral());
    }

    private void addGenotype(Genotype genotype) {
        if (genotypeMap.containsKey(genotype)) {
            int value = genotypeMap.get(genotype);
            genotypeMap.replace(genotype, value, value + 1);
        } else {
            genotypeMap.put(genotype, 1);
        }
    }

    public double getAverageLongevity() {
        return averageLongevity;
    }

    public double getAverageNumberOfPlants() {
        return averageNumberOfPlants;
    }

    public double getAverageEnergy() {
        return averageEnergy;
    }

    public double getAverageNumberOfChildren() {
        return averageNumberOfChildren;
    }

    public double getAverageNumberOfAnimals() {
        return averageNumberOfAnimals;
    }

    public double getAverageNumberOfPlantsInJungle() {
        return averageNumberOfPlantsInJungle;
    }

    public byte[] getMostPopularGenotype() {
        byte[] gens = new byte[32];
        Genotype mostPopularGenotype = new Genotype(gens);
        int value = 0;
        for (Map.Entry<Genotype, Integer> pair : genotypeMap.entrySet()) {
            if (pair.getValue() > value) {
                value = pair.getValue();
                mostPopularGenotype = pair.getKey();
            }
        }
        return mostPopularGenotype.getGenotype();
    }
}
