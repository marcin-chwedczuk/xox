package pl.marcinchwedczuk.xox.gui;

public class SearchStrategyData {
    public final SearchStrategyType searchStrategyType;

    public final int cutoffLevel;

    public final int minNumberOfMovesToEnableProbabilisticSearch;
    public final int percentageOfMovesToTry;

    public SearchStrategyData(SearchStrategyType searchStrategyType,
                              int cutoffLevel,
                              int minNumberOfMovesToEnableProbabilisticSearch,
                              int percentageOfMovesToTry) {
        this.searchStrategyType = searchStrategyType;
        this.cutoffLevel = cutoffLevel;
        this.minNumberOfMovesToEnableProbabilisticSearch = minNumberOfMovesToEnableProbabilisticSearch;
        this.percentageOfMovesToTry = percentageOfMovesToTry;
    }
}
