package statisticsObservers;

public interface IStatisticsObserver {
    public double getAverageLongevity();

    public double getAverageEnergy();

    public double getAverageNumberOfChildren();

    public byte[] getMostPopularGenotype();
}
