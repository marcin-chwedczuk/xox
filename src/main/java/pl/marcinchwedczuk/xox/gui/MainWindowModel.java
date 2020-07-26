package pl.marcinchwedczuk.xox.gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerComputerGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerHumanGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.GameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.HumanComputerGameMode;

public class MainWindowModel {
    public final ObservableList<GameGeometry> gameGeometries = FXCollections.observableArrayList(
            new GameGeometry(3, 3),
            new GameGeometry(4, 3),
            new GameGeometry(4, 4),
            new GameGeometry(5, 3),
            new GameGeometry(5, 4),
            new GameGeometry(5, 5)
    );
    public final ObjectProperty<GameGeometry> gameGeometryProperty =
            new SimpleObjectProperty<>(gameGeometries.get(0));

    public final ObjectProperty<GameModeType> gameModeProperty =
            new SimpleObjectProperty<>(GameModeType.HUMAN_COMPUTER);

    public final ObjectProperty<SearchStrategyType> searchStrategyProperty =
            new SimpleObjectProperty<>(SearchStrategyType.FULL_SEARCH);

    public final IntegerProperty minNumberOfMoves = new SimpleIntegerProperty(17);
    public final IntegerProperty percentageOfMoves = new SimpleIntegerProperty(40);

    public final ObservableList<Integer> cutoffLevels = FXCollections.observableArrayList(
            3, 4, 5
    );
    public final ObjectProperty<Integer> cutoffLevel = new SimpleObjectProperty<>(cutoffLevels.get(0));

    public final BooleanProperty emptyFieldsLoseProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty emptyFieldsWinsProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty countAlmostWinsProperty = new SimpleBooleanProperty(false);

    private final Dialogs dialogs;
    private final Logger logger;

    private XoXGame game;
    private GameMode gameMode;

    public Runnable modelChangedListener = () -> { };

    public MainWindowModel(Dialogs dialogs, Logger logger) {
        this.dialogs = dialogs;
        this.logger = logger;

        this.gameGeometryProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        this.gameModeProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        reset();
    }

    public void setModelChangedListener(Runnable action) {
        modelChangedListener = action;
    }

    private void notifyModelChanged() {
        modelChangedListener.run();
    }

    public void searchStrategyChanged(SearchStrategyData type) {
    }

    public void heuristicSettingsChanged(boolean emptyFieldsLose,
                                         boolean emptyFieldsWins,
                                         boolean countAlmostWins) {

    }

    public void nextMove() {
        logger.debug("========== %s =============", game.currentPlayer());
        try {
            gameMode.nextMove();
        }
        catch (Exception e) {
            dialogs.info(e.getMessage());
        }

        notifyModelChanged();
    }

    public void redoMove() {

    }

    public void reset() {
        var gameGeometry = gameGeometryProperty.get();
        game = new XoXGame(logger,
                gameGeometry.boardSize,
                gameGeometry.winningStride);

        gameMode = switch (gameModeProperty.get()) {
            case COMPUTER_COMPUTER ->
                    new ComputerComputerGameMode(logger, game);
            case HUMAN_COMPUTER ->
                    new HumanComputerGameMode(logger, dialogs, game);
            case COMPUTER_HUMAN ->
                    new ComputerHumanGameMode(logger, dialogs, game);
            default ->
                    throw new IllegalArgumentException();
        };

        gameMode.init();
        notifyModelChanged();
    }

    public Board board() {
        return game.board();
    }

    public void onBoardClicked(int row, int col) {
        try {
            gameMode.userClickedOnBoard(row, col);
        }
        catch (Exception e) {
            dialogs.info(e.getMessage());
        }

        notifyModelChanged();
    }
}
