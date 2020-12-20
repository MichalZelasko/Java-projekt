package simulation;

import worldMapElement.IMapElementObserver;

public interface ISimulationObserver extends IMapElementObserver {
    public void handleDayFinished();
}
