package world;

import configuration.Configuration;
import configuration.ConfigurationReader;
import map.RectangularMap;

import java.util.LinkedList;

public class World {
    public static void main(String[] args) {
        System.out.println("Hello world");
        String fileName="statistics.txt";
        ConfigurationReader mapConfiguration = new ConfigurationReader();
        Configuration configuration = mapConfiguration.getConfiguration();
        RectangularMap map = new RectangularMap(configuration.getMapWidth(), configuration.getMapHeight(), configuration.getJungleWidth(), configuration.getJungleHeight(), configuration.getNumberOfPlants(), configuration.getNumberOfAnimals(), configuration.getStartEnergy(), configuration, new LinkedList<>());
        FileWriter writer = new FileWriter(fileName, map);
        int signal=0;
        for (int i = 0; i < 300000; i++) {
            System.out.print(i + ". ");
            map.oneDayMore();
            map.printActualStatistics();
            map.printAverageStatistics();
            map.changeStatus(signal);
            writer.writeStatistics();
        }
        System.out.println("End of the World");
    }
}
