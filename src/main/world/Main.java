package world;

import configuration.Configuration;
import configuration.ConfigurationReader;
import javafx.application.Application;
import javafx.stage.Stage;
import simulation.Simulation;
import visualisation.Visualization;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("hello world");
        ConfigurationReader reader = new ConfigurationReader();
        Configuration configuration = reader.getConfiguration();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Simulation world = new Simulation();
        Visualization visualization = new Visualization(stage, world, world.getMap().getMapWidth(), world.getMap().getMapHeight(), world.getMap().getJungleLowerLeft(), world.getMap().getJungleUpperRight());
        world.startSimulation();
        visualization.render();
    }
}
