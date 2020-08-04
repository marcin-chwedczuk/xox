package pl.marcinchwedczuk.xox.gui;

import javafx.beans.property.*;
import pl.marcinchwedczuk.xox.game.XoXGameRules;
import pl.marcinchwedczuk.xox.game.heuristic.RationalPlayerHeuristics;

public class HeuristicsModel {
    public final ObjectProperty<GameGeometry> gameGeometryProperty =
            new SimpleObjectProperty<>();

    public final BooleanProperty countEmptyFieldsProperty =
            new SimpleBooleanProperty(true);

    public final BooleanProperty countAlmostWinsProperty =
            new SimpleBooleanProperty(false);

    private final ObjectProperty<RationalPlayerHeuristics> heuristicsProperty =
            new SimpleObjectProperty<>();

    public HeuristicsModel() {
        gameGeometryProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        countAlmostWinsProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        countEmptyFieldsProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });
    }

    private void reset() {
        var geometry = gameGeometryProperty.get();
        if (geometry == null) {
            heuristicsProperty.set(null);
            return;
        }

        var heuristics = new RationalPlayerHeuristics(
                new XoXGameRules(geometry.boardSize, geometry.winningStride));

        heuristics.setCountEmptyFields(countEmptyFieldsProperty.get());
        heuristics.setCountAlmostWins(countAlmostWinsProperty.get());

        heuristicsProperty.set(heuristics);
    }

    public ReadOnlyObjectProperty<RationalPlayerHeuristics> heuristicsProperty() {
        return heuristicsProperty;
    }
}
