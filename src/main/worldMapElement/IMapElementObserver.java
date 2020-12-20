package worldMapElement;

import map.MapElementAction;

public interface IMapElementObserver {
    void handleElementChange(IMapElement eventTarget, MapElementAction context, Object oldValue);
}
