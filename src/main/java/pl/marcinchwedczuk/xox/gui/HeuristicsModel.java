package pl.marcinchwedczuk.xox.gui;

import javafx.beans.property.*;
import pl.marcinchwedczuk.xox.game.XoXGameRules;
import pl.marcinchwedczuk.xox.game.heuristic.HeuristicsImpl;

public class HeuristicsModel {
    public final ObjectProperty<GameGeometry> gameGeometryProperty =
            new SimpleObjectProperty<>();

    public final BooleanProperty countEmptyFieldsOnLooseProperty =
            new SimpleBooleanProperty(true);

    public final BooleanProperty countEmptyFieldsOnWinProperty =
            new SimpleBooleanProperty(true);

    public final BooleanProperty countAlmostWinsProperty =
            new SimpleBooleanProperty(false);

    private final ObjectProperty<HeuristicsImpl> heuristicsProperty =
            new SimpleObjectProperty<>();

    public HeuristicsModel() {
        gameGeometryProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        countAlmostWinsProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        countEmptyFieldsOnLooseProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        countEmptyFieldsOnWinProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });
    }

    private void reset() {
        var geometry = gameGeometryProperty.get();
        if (geometry == null) {
            heuristicsProperty.set(null);
            return;
        }

        var scorer = new HeuristicsImpl(
                new XoXGameRules(geometry.boardSize, geometry.winningStride));

        scorer.setCountEmptyFieldsOnLoose(countEmptyFieldsOnLooseProperty.get());
        scorer.setCountEmptyFieldsOnWin(countEmptyFieldsOnWinProperty.get());
        scorer.setCountAlmostWins(countAlmostWinsProperty.get());

        heuristicsProperty.set(scorer);
    }

    public ReadOnlyObjectProperty<HeuristicsImpl> heuristicsProperty() {
        return heuristicsProperty;
    }
}
