package pl.marcinchwedczuk.xox.gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.marcinchwedczuk.xox.game.search.CutoffStrategy;
import pl.marcinchwedczuk.xox.game.search.FullSearch;
import pl.marcinchwedczuk.xox.game.search.ProbabilisticSearch;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;

public class SearchStrategyModel {
    public final ObjectProperty<StrategyType> searchStrategyTypeProperty =
            new SimpleObjectProperty<>(StrategyType.FULL_SEARCH);

    // Probabilistic Strategy Properties
    public final IntegerProperty probabilisticSearch_numberOfMovesProperty =
            new SimpleIntegerProperty(17);

    public final IntegerProperty probabilisticSearch_cutoffLevelProperty =
            new SimpleIntegerProperty(0);

    // Cutoff strategy properties
    public final ObservableList<Integer> cutoffSearch_cutoffLevelsProperty = FXCollections.observableArrayList(
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
    );

    public final ObjectProperty<Integer> cutoffSearch_cutoffLevelProperty =
            new SimpleObjectProperty<>(cutoffSearch_cutoffLevelsProperty.get(0));

    private final ObjectProperty<SearchStrategy> searchStrategyProperty =
            new SimpleObjectProperty<>();

    public SearchStrategyModel() {
        searchStrategyTypeProperty.addListener((observable, oldValue, newValue) -> {
                resetStrategy();
        });

        probabilisticSearch_numberOfMovesProperty.addListener((observable, oldValue, newValue) -> {
            resetStrategy();
        });

        probabilisticSearch_cutoffLevelProperty.addListener((observable, oldValue, newValue) -> {
            resetStrategy();
        });

        cutoffSearch_cutoffLevelProperty.addListener((observable, oldValue, newValue) -> {
            resetStrategy();
        });

        resetStrategy();
    }

    private void resetStrategy() {
        switch (searchStrategyTypeProperty.get()) {
            case FULL_SEARCH:
                searchStrategyProperty.set(new FullSearch());
                break;

            case CUT_OFF: {
                var strategy = CutoffStrategy.basedOn(new FullSearch());
                strategy.setCutoff(cutoffSearch_cutoffLevelProperty.get());
                searchStrategyProperty.set(strategy);
                break;
            }

            case PROBABILISTIC: {
                var strategy = ProbabilisticSearch.basedOn(new FullSearch());
                strategy.setNumberOfMoves(probabilisticSearch_numberOfMovesProperty.get());
                strategy.setCutoff(probabilisticSearch_cutoffLevelProperty.get());
                searchStrategyProperty.set(strategy);
                break;
            }

            default:
                throw new IllegalArgumentException();
        }
    }

    public ReadOnlyObjectProperty<SearchStrategy> strategyProperty() {
        return searchStrategyProperty;
    }
}
