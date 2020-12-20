package visualisation;

import simulation.ISimulationObserver;
import worldMapElement.Animal;
import map.Vector2d;
import map.MapElementAction;
import worldMapElement.IMapElement;
import worldMapElement.Plant;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import utils.PhotoManager;

import java.util.*;

public class MapVisualizer implements ISimulationObserver {
    private static final String earthTexturePath = "earth-32.jpg";
    private static final String grassTexturePath = "grass-32.jpg";
    private static final String animalTexturePath = "animal-32.jpg";
    private static final String jungleTexturePath = "jungle-32.jpg";
    private static final String superSaiyanTexturePath = "earth-32.jpg";
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final GridPane mapVisualization;
    private final Map<Vector2d, FieldType> currentFields;
    private final Map<Vector2d, ImageView> currentTextures;
    private final Map<Vector2d, FieldType> fieldsToUpdate;
    private final Map<Vector2d, Set<Animal>> animals;
    private final Map<Vector2d, Plant> plants;

    public MapVisualizer(int mapWidth, int mapHeight, Vector2d jungleLowerLeft, Vector2d jungleUpperRight) {
        this.mapVisualization = new GridPane();
        this.currentTextures = new HashMap<>();
        this.fieldsToUpdate = new HashMap<>();
        this.currentFields = new HashMap<>();
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;
        initMap(mapWidth, mapHeight, jungleLowerLeft, jungleUpperRight, mapVisualization);
    }

    private void initMap(int width, int height, Vector2d jungleLowerLeft, Vector2d jungleUpperRight, GridPane mapGrid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d pos = new Vector2d(x, y);
                ImageView image;
                if (isInJungle(pos)) {
                    image = getTexture(FieldType.JUNGLE);
                } else {
                    image = getTexture(FieldType.STEPPE);
                }
                currentTextures.put(pos, image);
                mapGrid.add(image, x, y);
            }
        }
        mapGrid.setAlignment(Pos.CENTER);
    }

    public void handleDayFinished() {
        Map<Vector2d, FieldType> copyFieldsToUpdate = new HashMap<>(fieldsToUpdate);
        Platform.runLater(() -> {
            for (Vector2d field: copyFieldsToUpdate.keySet()) {
                if (currentFields.get(field) == copyFieldsToUpdate.get(field)) {
                    continue;
                }
                ImageView texture = getTexture(copyFieldsToUpdate.get(field));
                mapVisualization.getChildren().remove(currentTextures.get(field));
                mapVisualization.add(texture, field.x, field.y);
                currentTextures.put(field, texture);
            }
        });
        fieldsToUpdate.clear();
    }


    public void handleElementChange(IMapElement eventTarget, MapElementAction context, Object oldValue) {
        switch (context) {
            case ANIMAL_BIRTH : {
                Animal born = (Animal) eventTarget;
                Vector2d field = born.getPosition();
                animals.putIfAbsent(field, new HashSet<>());
                animals.get(field).add(born);
                updateFieldIfNeeded(field);
                break;
            }
            case NEW_PLANT : {
                Plant added = (Plant) eventTarget;
                Vector2d field = added.getPosition();
                plants.put(field, added);
                updateFieldIfNeeded(field);
                break;
            }
            case POSITION_CHANGE : {
                Animal animal = (Animal) eventTarget;
                Vector2d oldPosition = (Vector2d) oldValue;
                animals.get(oldPosition).remove(animal);
                animals.putIfAbsent(animal.getPosition(), new HashSet<>());
                animals.get(animal.getPosition()).add(animal);
                updateFieldIfNeeded(oldPosition);
                updateFieldIfNeeded(animal.getPosition());
                break;
            }
            case ANIMAL_DEATH : {
                System.out.println("Animal Death");
                Animal deceased = (Animal) eventTarget;
                Vector2d field = deceased.getPosition();
                this.animals.get(field).remove(deceased);
                updateFieldIfNeeded(field);
                break;
            }
            case PLANT_CONSUMPTION : {
                System.out.println("Plant Consumption");
                Plant eaten = (Plant) eventTarget;
                Vector2d field = eaten.getPosition();
                plants.remove(field);
                updateFieldIfNeeded(field);
                break;
            }
        }
    }

    private ImageView getTexture(FieldType fieldType) {
        ImageView view = null;
        switch (fieldType) {
            case PLANT : view = PhotoManager.getView(grassTexturePath); break;
            case JUNGLE : view = PhotoManager.getView(jungleTexturePath); break;
            case STEPPE : view = PhotoManager.getView(earthTexturePath); break;
            case ANIMAL_LOW_ENERGY : view = PhotoManager.getView(animalTexturePath); break;
            case ANIMAL_MID_ENERGY : view = PhotoManager.getView(animalTexturePath); break;
            case ANIMAL_HIGH_ENERGY : view = PhotoManager.getView(animalTexturePath); break;
            case ANIMAL_ENERGY_OVER_9000 : view = PhotoManager.getView(superSaiyanTexturePath); break;
        };
        return view;
    }

    private void updateFieldIfNeeded(Vector2d field) {
        FieldType current = currentFields.get(field);
        FieldType updated = isInJungle(field) ? FieldType.JUNGLE : FieldType.STEPPE;
        if (plants.get(field) != null) {
            updated = FieldType.PLANT;
        }
        animals.putIfAbsent(field, new HashSet<>());
        if (!animals.get(field).isEmpty()) {
            updated = FieldType.ANIMAL_LOW_ENERGY;
        }
        if (current != updated) {
            fieldsToUpdate.put(field, updated);
        }
    }

    private boolean isInJungle(Vector2d pos) {
        return jungleLowerLeft.precedes(pos) && jungleUpperRight.follows(pos);
    }

    public GridPane getMapVisualization() {
        return mapVisualization;
    }
}
