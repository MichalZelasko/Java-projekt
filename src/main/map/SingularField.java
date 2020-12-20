package map;

import worldMapElement.Animal;
import worldMapElement.Plant;

import java.util.ArrayList;
import java.util.List;


public class SingularField {
    private final Vector2d position;
    private List<Animal> animalsWithMaxEnergy = new ArrayList<Animal>();
    private List<Animal> animalsWithSecondEnergy = new ArrayList<Animal>();
    private Plant plant = null;
    private RectangularMap map;

    public SingularField(Vector2d position, RectangularMap map) {
        this.position = position;
        this.map = map;
    }

    public boolean getPlantPresence() {
        return plant != null;
    }

    public int eatPlant(int plantEnergy) {
        if (!getPlantPresence() || animalsWithMaxEnergy.isEmpty())
            return 0;
        int numberOfAnimals = animalsWithMaxEnergy.size();
        for (Animal a1 : animalsWithMaxEnergy) {
            a1.eat(plantEnergy / numberOfAnimals);
        }
        plant.getEaten();
        plant = null;
        if (map.isJunglePosition(position)) {
            map.removePlantInsideJungle(position);
        } else {
            map.removePlant(position);
        }
        return plantEnergy;
    }

    public Animal reproduction(int minEnergy) {
        if (animalsWithMaxEnergy.size() + animalsWithSecondEnergy.size() < 2) {
            return null;
        }
        Animal a1;
        Animal a2;
        a1 = animalsWithMaxEnergy.get(0);
        if (animalsWithMaxEnergy.size() > 1) {
            a2 = animalsWithMaxEnergy.get(1);
        } else {
            a2 = animalsWithSecondEnergy.get(0);
        }
        if (minEnergy < a2.getEnergy()) {
            return a1.reproduceAnimal(a2);
        }
        return null;
    }

    public void cleanUpField() {
        animalsWithMaxEnergy = new ArrayList<>();
        animalsWithSecondEnergy = new ArrayList<>();
    }

    public void addAnimal(Animal a) {
        if (animalsWithMaxEnergy.size() == 0 || animalsWithMaxEnergy.get(0).getEnergy() < a.getEnergy()) {
            animalsWithSecondEnergy = animalsWithMaxEnergy;
            animalsWithMaxEnergy = new ArrayList<>();
            animalsWithMaxEnergy.add(a);
        } else if (animalsWithMaxEnergy.get(0).getEnergy() == a.getEnergy()) {
            animalsWithMaxEnergy.add(a);
        } else if (animalsWithSecondEnergy.size() == 0 || animalsWithSecondEnergy.get(0).getEnergy() < a.getEnergy()) {
            animalsWithSecondEnergy = new ArrayList<>();
            animalsWithSecondEnergy.add(a);
        } else if (animalsWithSecondEnergy.get(0).getEnergy() == a.getEnergy()) {
            animalsWithSecondEnergy.add(a);
        }
    }

    public Object objectAt() {
        if (animalsWithMaxEnergy.isEmpty()) {
            return null;
        }
        return animalsWithMaxEnergy.get(0);
    }

    public void putPlant() {
        plant = new Plant(position, map);
        map.addPlant(plant);
    }
}
