package world;

import map.RectangularMap;

import java.io.PrintWriter;
import java.util.Arrays;

public class FileWriter {
    private String filename;
    private RectangularMap map;

    public FileWriter(String filename, RectangularMap map) {
        this.filename = filename;
        this.map = map;
    }

    public void writeStatistics() {
        try {
            PrintWriter result = new PrintWriter(filename);
            result.println("Number Of plants in jungle: " + map.getJunglePlantNumber());
            result.println("Total number of plants in jungle: " + map.getPlantNumber());
            result.println("Total number of animals: " + map.getNumberOfAnimals());
            result.print("Animals average longevity: ");
            result.format("%.2f", map.getAverageLongevity());
            result.println("");
            result.print("Animals average energy: ");
            result.format("%.2f ", map.getAverageEnergy());
            result.println("");
            result.print("Animals average number of children: ");
            result.format("%.2f", map.getAverageNumberOfChildren());
            result.println("");
            result.println("Average genotype: ");
            result.println(Arrays.toString(map.getMostPopularGenotype()));
            result.println("Most frequent genotype: ");
            result.println(Arrays.toString(map.getAverageGenotype()));
            result.println();
            result.close();
        } catch (Exception ex) {
            System.out.println("There's no file to save results.");
        }
    }
}
