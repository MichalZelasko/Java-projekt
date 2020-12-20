package worldMapElement;
import map.RectangularMap;

public class ChosenAnimalObserver {

    private int numberOfChildren;
    private int numberOfSuccessors;
    private int animalDeathTime;
    private RectangularMap map;
    private ChosenAnimalObserver parent = null;
    private int numberOfDaysToEnd;

    public ChosenAnimalObserver(RectangularMap map, int number){
        int numberOfChildren = 0;
        int numberOfSuccessors = 0;
        int animalDeathTime = -1;
        this.map = map;
        this.numberOfDaysToEnd = number;
    }

    public ChosenAnimalObserver(RectangularMap map, ChosenAnimalObserver observer){
        int numberOfChildren = 0;
        int numberOfSuccessors = 0;
        int animalDeathTime = -1;
        this.map = map;
        this.parent = observer;
        this.numberOfDaysToEnd = observer.getNumberOfDaysToEnd();
    }

    public void addChild() {
        numberOfChildren++;
    }

    public void addSuccessor() {
        numberOfSuccessors++;
        if(parent != null) {
            parent.addSuccessor();
        }
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public int getNumberOfSuccessors() {
        return numberOfSuccessors;
    }

    public int getNumberOfDaysToEnd() {
        return numberOfDaysToEnd;
    }

    public int getDeathTime() {
        return animalDeathTime;
    }

    public void setDeathTime() {
        animalDeathTime = map.getActualTime();
    }

    public boolean check() {
        numberOfDaysToEnd--;
        return numberOfDaysToEnd <= 0;
    }
}
