package statisticsObservers;

import java.util.HashMap;
import java.util.Map;
import worldMapElement.Genotype;
import map.RectangularMap;
import worldMapElement.Animal;

public class ActualStatistics implements IStatisticsObserver{
    private double averageLongevity;
    private int numberOfDeadAnimals;
    private long totalEnergy;
    private long totalNumberOfChildren;
    private long[] sumOfGensNumber;
    private HashMap<Genotype, Integer> genotypeMap;
    private RectangularMap map;

    public ActualStatistics(int numberOfAnimals, int startEnergy, RectangularMap map) {
        this.map = map;
        numberOfDeadAnimals = 0;
        averageLongevity = 0.0;
        totalNumberOfChildren = 0;
        genotypeMap = new HashMap<>();
        totalEnergy = numberOfAnimals * startEnergy;
        sumOfGensNumber = new long[8];
        for (int i = 0; i < 8; i++) {
            sumOfGensNumber[i] = 0;
        }
    }

    public double getAverageLongevity() {
        return averageLongevity;
    }

    public double getAverageEnergy() {
        return (double) totalEnergy / (double) map.getAnimals().size();
    }

    public double getAverageNumberOfChildren() {
        return (double) totalNumberOfChildren / (double) map.getAnimals().size();
    }


    public byte[] getAverageGenotype() {
        int[] counterArray = new int[8];
        for (int i = 0; i < 8; i++) {
            counterArray[i] = (int) sumOfGensNumber[i] / map.getAnimals().size();
        }
        byte[] averageGenotype = new byte[32];
        int j = 0;
        for (int i = 0; i < 8; i++) {
            while (counterArray[i] > 0 && j < 32) {
                counterArray[i]--;
                averageGenotype[j] = (byte) i;
                j++;
            }
        }
        while (j < 32) {
            averageGenotype[j] = 7;
            j++;
        }
        return averageGenotype;
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

    public void actualizeStatisticsDead(Animal iterator) {
        averageLongevity = (averageLongevity * numberOfDeadAnimals + iterator.getLongevity()) / (numberOfDeadAnimals + 1);
        numberOfDeadAnimals++;
        removeGenotype(iterator.getGenotypeGeneral());
        totalNumberOfChildren = totalNumberOfChildren - iterator.getNumberOfChildren();
        for (byte i : iterator.getGenotype()) {
            sumOfGensNumber[i]--;
        }
    }

    public void actualizeStatisticsLife(int liveEnergy){
        totalEnergy = totalEnergy - map.getAnimals().size() * liveEnergy;
    }

    public void actualizeStatisticsConsumption(int eatenPlantEnergy) {
        totalEnergy = totalEnergy + eatenPlantEnergy;
    }

    public void actualizeStatisticsBirth() {
        totalNumberOfChildren = totalNumberOfChildren + 2;
    }

    public void actualizeStatisticsNewAnimal(Animal animal) {
        for (byte i : animal.getGenotype()) {
            sumOfGensNumber[i]++;
        }
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

    private void removeGenotype(Genotype genotype) {
        int value = genotypeMap.get(genotype);
        genotypeMap.remove(genotype);
        if (value > 1) {
            genotypeMap.put(genotype, value - 1);
        }
    }

}
