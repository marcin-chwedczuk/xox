package pl.marcinchwedczuk.xox.gui;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerComputerGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerHumanGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.GameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.HumanComputerGameMode;

import java.util.concurrent.CompletableFuture;

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

    public final BooleanProperty inputEnabled = new SimpleBooleanProperty(true);
    public final IntegerProperty progress = new SimpleIntegerProperty(0);

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
                    new HumanComputerGameMode(logger, game);
            case COMPUTER_HUMAN ->
                    new ComputerHumanGameMode(logger, game);
            default ->
                    throw new IllegalArgumentException();
        };

        gameMode.init();
        notifyModelChanged();
    }

    public Board board() {
        return game.board();
    }

    public void nextMove() {
        logger.debug("========== %s =============", game.currentPlayer());

        // CompletableFuture == poor's man Try[V]
        Task<CompletableFuture<Void>> nextMoveTask = new Task<>() {
            @Override
            protected CompletableFuture<Void> call() {
                try {
                    gameMode.nextMove();
                    return CompletableFuture.completedFuture(null);
                }
                catch (Exception ex) {
                    return CompletableFuture.failedFuture(ex);
                }
            }
        };

        progress.bind(nextMoveTask.progressProperty());
        inputEnabled.setValue(false);

        nextMoveTask.setOnSucceeded(event -> {
            progress.unbind();
            inputEnabled.setValue(true);

            notifyModelChanged();

            nextMoveTask.getValue().exceptionally(ex -> {
                dialogs.exception(ex);
                return null;
            });
        });

        var t = new Thread(nextMoveTask);
        t.setDaemon(true);
        t.setName("compute-next-move-thread");
        t.start();
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
