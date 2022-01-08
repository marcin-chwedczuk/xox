package pl.marcinchwedczuk.xox.gui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.marcinchwedczuk.xox.game.GameState;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.gui.gamemode.*;
import pl.marcinchwedczuk.xox.mvvm.AsyncCommand;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Logger;
import pl.marcinchwedczuk.xox.util.Unit;

public class MainWindowModel {
    public final ObservableList<GameGeometry> gameGeometriesProperty = FXCollections.observableArrayList(
            new GameGeometry(3, 3),
            new GameGeometry(4, 3),
            new GameGeometry(4, 4),
            new GameGeometry(5, 3),
            new GameGeometry(5, 4),
            new GameGeometry(5, 5)
    );
    public final ObjectProperty<GameGeometry> gameGeometryProperty =
            new SimpleObjectProperty<>(gameGeometriesProperty.get(0));

    public final ObjectProperty<GameModeType> gameModeProperty =
            new SimpleObjectProperty<>(GameModeType.HUMAN_COMPUTER);

    public final SearchStrategyModel searchStrategyModel = new SearchStrategyModel();
    public final HeuristicsModel heuristicsModel = new HeuristicsModel();

    public final StringProperty turnProperty = new SimpleStringProperty();
    public final StringProperty markProperty = new SimpleStringProperty();

    public final ObjectProperty<GameState> gameStateProperty =
            new SimpleObjectProperty<>(null);

    public final AsyncCommand<Either<ErrorMessage, Unit>> nextMoveCommand;
    public final BooleanProperty canUndo = new SimpleBooleanProperty(false);

    private final Logger logger;
    private final Dialogs dialogs;

    private XoXGame game;
    private GameMode gameMode;

    public Runnable modelChangedListener = () -> { };

    public MainWindowModel(Dialogs dialogs, Logger logger) {
        this.dialogs = dialogs;
        this.logger = logger;

        this.gameGeometryProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });
        this.heuristicsModel.gameGeometryProperty
                .bindBidirectional(this.gameGeometryProperty);

        this.gameModeProperty.addListener((observable, oldValue, newValue) -> {
            reset();
        });

        searchStrategyModel.strategyProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Set search strategy to %s", newValue);
            game.setSearchStrategy(newValue);
        });

        heuristicsModel.heuristicsProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("Set heuristics to %s", newValue);
            game.setBoardScorer(newValue);
        });

        // Commands
        BooleanBinding nextMoveEnabled = Bindings.createBooleanBinding(() -> {
            var gameState = gameStateProperty.getValue();
            return (gameState == null) || !gameState.isFinished;
        }, gameStateProperty);

        this.nextMoveCommand = new AsyncCommand<>(
                cancelOp -> gameMode.performComputerMove(cancelOp),
                nextMoveEnabled);

        nextMoveCommand.resultProperty()
                .addListener((observable, oldValue, newValue) -> {
                    handleModelChanges();

                    // TODO: This sucks, needs some refactor
                    newValue.onRight(result -> {
                        result.onLeft(errorMessage -> {
                            Platform.runLater(() -> dialogs.info(errorMessage.message));
                        });
                    });
                });

        reset();
    }

    public void undo() {
        game.undoMove();
        handleModelChanges();
    }

    public void reset() {
        var gameGeometry = gameGeometryProperty.get();
        logger.debug("Game geometry: %s", gameGeometry);

        var searchStrategy = searchStrategyModel.strategyProperty().get();
        logger.debug("Search strategy: %s", searchStrategy);

        var boardScorer = heuristicsModel.heuristicsProperty().get();
        logger.debug("Scorer is: %s", boardScorer);

        game = new XoXGame(logger,
                gameGeometry.boardSize, gameGeometry.winningStride,
                boardScorer, searchStrategy);

        resetGameMode();

        // Notify to redraw
        handleModelChanges();
    }

    private void resetGameMode() {
        switch (gameModeProperty.get()) {
            case COMPUTER_COMPUTER:
                gameMode = new ComputerComputerGameMode(logger, game);
                break;
            case HUMAN_COMPUTER:
                gameMode = new HumanComputerGameMode(logger, game);
                break;
            case COMPUTER_HUMAN:
                gameMode = new ComputerHumanGameMode(logger, game);
                break;
            default:
                throw new IllegalArgumentException();
        }

        gameMode.init();
        updateGameModeProperties();
    }

    public void onBoardClicked(int row, int col) {
        var result = gameMode.performHumanMove(row, col);
        result.onLeft(msg -> dialogs.info(msg.message));

        handleModelChanges();
    }

    private void handleModelChanges() {
        gameStateProperty.set(game.state());
        canUndo.set(game.canUndoMove());
        updateGameModeProperties();
        modelChangedListener.run();
    }

    private void updateGameModeProperties() {
        turnProperty.set(gameMode.currentPlayer());
        markProperty.set(gameMode.currentPlayerMark().toString());
    }
}
