package worldMapElement;

import map.MapElementAction;
import map.Vector2d;
import map.RectangularMap;
import simulation.ISimulationObserver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

abstract public class AbstractMapElement implements IMapElement {

    protected Vector2d position;
    protected final RectangularMap map;
    protected final List<IMapElementObserver> observers;

    public AbstractMapElement(Vector2d position, RectangularMap map) {
        this.observers = new LinkedList<>();
        observers.addAll(map.getObservers());
        this.position = position;
        this.map = map;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void addObserver(IMapElementObserver observer) {
        this.observers.add(observer);
    }

    public void addAllObservers(Collection<? extends IMapElementObserver> observers) {
        this.observers.addAll(observers);
    }

    public void removeObserver(IMapElementObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(MapElementAction context, Object oldValue) {
        for (IMapElementObserver observer : this.observers) {
            observer.handleElementChange(this, context, oldValue);
        }
    }
}
