package worldMapElement;

import map.MapElementAction;
import map.Vector2d;
import map.RectangularMap;

public class Plant extends AbstractMapElement {

    public Plant(Vector2d position, RectangularMap map) {
        super(position, map);
        notifyObservers(MapElementAction.NEW_PLANT, null);
    }

    public void getEaten() {
        notifyObservers(MapElementAction.PLANT_CONSUMPTION, null);
    }
}
