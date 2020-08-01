package pl.marcinchwedczuk.xox.gui;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.marcinchwedczuk.xox.game.search.CutoffStrategy;
import pl.marcinchwedczuk.xox.game.search.FullSearch;
import pl.marcinchwedczuk.xox.game.search.ProbabilisticSearch;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;

public class SearchStrategyModel {
    public final ObjectProperty<StrategyType> strategyTypeProperty =
            new SimpleObjectProperty<>(StrategyType.FULL_SEARCH);

    // Probabilistic Strategy Properties
    public final IntegerProperty minNumberOfMovesProperty =
            new SimpleIntegerProperty(17);

    public final IntegerProperty percentageOfMovesProperty =
            new SimpleIntegerProperty(40);

    // Cutoff strategy properties
    public final ObservableList<Integer> cutoffLevelsProperty = FXCollections.observableArrayList(
            1, 2, 3, 4, 5
    );

    public final ObjectProperty<Integer> cutoffLevelProperty =
            new SimpleObjectProperty<>(cutoffLevelsProperty.get(0));

    private final ObjectProperty<SearchStrategy> strategyProperty =
            new SimpleObjectProperty<>();

    public SearchStrategyModel() {
        strategyTypeProperty.addListener((observable, oldValue, newValue) -> {
                resetStrategy();
        });

        minNumberOfMovesProperty.addListener((observable, oldValue, newValue) -> {
            resetStrategy();
        });

        percentageOfMovesProperty.addListener((observable, oldValue, newValue) -> {
            resetStrategy();
        });

        cutoffLevelProperty.addListener((observable, oldValue, newValue) -> {
            resetStrategy();
        });

        resetStrategy();
    }

    private void resetStrategy() {
        switch (strategyTypeProperty.get()) {
            case FULL_SEARCH:
                strategyProperty.set(new FullSearch());
                break;

            case CUT_OFF: {
                var strategy = CutoffStrategy.basedOn(new FullSearch());
                strategy.setCutoff(cutoffLevelProperty.get());
                strategyProperty.set(strategy);
                break;
            }

            case PROBABILISTIC: {
                var strategy = ProbabilisticSearch.basedOn(new FullSearch());
                strategy.setMinNumberOfMoves(minNumberOfMovesProperty.get());
                strategy.setPercentageOfMovesToCheck(percentageOfMovesProperty.get());
                strategyProperty.set(strategy);
                break;
            }

            default:
                throw new IllegalArgumentException();
        }
    }

    public ReadOnlyObjectProperty<SearchStrategy> strategyProperty() {
        return strategyProperty;
    }
}
