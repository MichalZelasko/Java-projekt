package worldMapElement;

import java.util.Objects;
import java.util.Random;

import map.MapDirection;
import map.MapElementAction;
import map.RectangularMap;
import map.Vector2d;
import simulation.ISimulationObserver;

import static java.lang.Math.abs;

public class Animal extends AbstractMapElement {

    private MapDirection direction;
    private Genotype genotype;
    private int actualEnergy;
    private int longevity = 0;
    private int numberOfChildren = 0;
    private ChosenAnimalObserver observer = null;

    public Animal(RectangularMap map, Vector2d initialPosition, Genotype newGenotype, int startEnergy) {
        super(initialPosition, map);
        direction = MapDirection.NORTH;
        genotype = newGenotype;
        actualEnergy = startEnergy;
        notifyObservers(MapElementAction.ANIMAL_BIRTH, null);
    }

    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return position.toString() + direction.toString() + " " + getEnergy();
    }

    public void move(int mapWidth, int mapHeight) {
        Random generator = new Random();
        byte command = genotype.getDirection(abs(generator.nextInt()) % 32);
        direction = direction.rotation(command);
        Vector2d oldPosition = position;
        position = position.add(direction.toVector()).limitWithMapBounds(new Vector2d(mapWidth, mapHeight));
        this.notifyObservers(MapElementAction.POSITION_CHANGE, oldPosition);
    }

    public void eat(int additionalEnergy) {
        actualEnergy = actualEnergy + additionalEnergy;
    }

    public void live(int energyForLife) {
        longevity++;
        actualEnergy = actualEnergy - energyForLife;
        if(observer != null && observer.check()) {
            removeChosenAnimalObserver();
        }
    }

    public byte[] getGenotype() {
        return genotype.getGenotype();
    }

    public Genotype getGenotypeGeneral() {
        return genotype;
    }

    public int getEnergy() {
        return actualEnergy;
    }

    public int getLongevity() {
        return longevity;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public Genotype reproduceGenotype(Animal a2) {
        return genotype.reproduceGenotype(a2);
    }

    public int influenceEnergy() {
        int resultEnergy = actualEnergy / 4;
        actualEnergy = actualEnergy - resultEnergy;
        return resultEnergy;
    }

    public int reproduceEnergy(Animal a2) {
        return a2.influenceEnergy() + influenceEnergy();
    }

    public Vector2d reproducePosition() {
        Random generator = new Random();
        int dir = abs(generator.nextInt()) % 8;
        Vector2d v1;
        for (int i = 0; i < 8; i++) {
            v1 = position.add(MapDirection.NORTH.rotation((byte) ((dir + i) % 8)).toVector()).limitWithMapBounds(new Vector2d(map.getMapWidth() - 1, map.getMapHeight() - 1));
            if (!(map.objectAt(v1) instanceof Animal)) {
                return v1;
            }
        }
        return position.add(MapDirection.NORTH.rotation((byte) (dir)).toVector()).limitWithMapBounds(new Vector2d(map.getMapWidth() - 1, map.getMapHeight() - 1));
    }

    public void haveNewChild() {
        numberOfChildren++;
    }

    public Animal reproduceAnimal(Animal a2) {
        haveNewChild();
        a2.haveNewChild();
        Animal result =  new Animal(map, this.reproducePosition(), reproduceGenotype(a2), this.reproduceEnergy(a2));
        notifyChosenAnimalObserver();
        a2.notifyChosenAnimalObserver();
        if(observer != null) {
            ChosenAnimalObserver newObserver = new ChosenAnimalObserver(map, observer);
            result.addChosenAnimalObserver(newObserver);
        }
        else if(a2.observer != null) {
            ChosenAnimalObserver newObserver = new ChosenAnimalObserver(map, a2.observer);
            result.addChosenAnimalObserver(newObserver);
        }
        return result;
    }

    public void die() {
        if(observer == null) {
            return;
        }
        this.observer.setDeathTime();
        this.notifyObservers(MapElementAction.ANIMAL_DEATH, null);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Animal))
            return false;
        Animal that = (Animal) other;
        return this.position.equals(that.position) && this.direction == that.direction;
    }

    public int hashCode() {
        return Objects.hash(direction, position.x, position.y, longevity, actualEnergy, numberOfChildren);
    }

    public void addChosenAnimalObserver(ChosenAnimalObserver observer) {
        this.observer = observer;
    }

    public void removeChosenAnimalObserver() {
        this.observer = null;
    }

    public void notifyChosenAnimalObserver() {
        if(observer == null) {
            return;
        }
        this.observer.addChild();
        this.observer.addSuccessor();
    }
}