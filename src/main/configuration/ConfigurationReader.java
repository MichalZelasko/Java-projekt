package configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

import static java.lang.System.exit;

public class ConfigurationReader {

    private static final String filePath = "configuration.json";
    private Configuration configuration;

    public ConfigurationReader() {
        long mapWidth = 0;
        long mapHeight = 0;
        long startEnergy = 0;
        long moveEnergy = 0;
        long plantEnergy = 0;
        double jungleRatio = 0;
        long numberOfPlants = 0;
        long numberOfAnimals = 0;
        try (FileReader reader = new FileReader(ClassLoader.getSystemResource(filePath).getFile())) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            mapWidth = (long) jsonObject.get("width");
            mapHeight = (long) jsonObject.get("height");
            startEnergy = (long) jsonObject.get("startEnergy");
            moveEnergy = (long) jsonObject.get("moveEnergy");
            plantEnergy = (long) jsonObject.get("plantEnergy");
            jungleRatio = (double) jsonObject.get("jungleRatio");
            numberOfPlants = (long) jsonObject.get("numberOfPlants");
            numberOfAnimals = (long) jsonObject.get("numberOfAnimals");
        } catch (Exception ex) {
            System.out.print("JSON file error. Incorrect data in configuration.json");
            exit(1);
        }
        configuration = new Configuration((int) mapWidth, (int) mapHeight, (int) startEnergy, (int) moveEnergy, (int) plantEnergy, (float) jungleRatio, (int) numberOfAnimals, (int) numberOfPlants);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}