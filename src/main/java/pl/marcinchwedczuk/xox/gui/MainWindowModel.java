package pl.marcinchwedczuk.xox.gui;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.game.search.CutoffStrategy;
import pl.marcinchwedczuk.xox.game.search.FullSearch;
import pl.marcinchwedczuk.xox.game.search.ProbabilisticSearch;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerComputerGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerHumanGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.GameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.HumanComputerGameMode;
import pl.marcinchwedczuk.xox.mvvm.AsyncCommand;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Unit;

import java.util.Timer;
import java.util.TimerTask;
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
            1, 2, 3, 4, 5
    );
    public final ObjectProperty<Integer> cutoffLevel = new SimpleObjectProperty<>(cutoffLevels.get(0));

    public final BooleanProperty emptyFieldsLoseProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty emptyFieldsWinsProperty = new SimpleBooleanProperty(true);
    public final BooleanProperty countAlmostWinsProperty = new SimpleBooleanProperty(false);

    public final AsyncCommand<Either<ErrorMessage, Unit>> nextMoveCommand;

    private final Dialogs dialogs;
    private final Logger logger;

    private XoXGame game;
    private SearchStrategy searchStrategy;
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

        this.nextMoveCommand = new AsyncCommand<>(
                cancelOp -> gameMode.performComputerMove(cancelOp),
                new SimpleBooleanProperty(true));

        nextMoveCommand.resultProperty()
                .addListener((observable, oldValue, newValue) -> notifyModelChanged());


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
        searchStrategy = switch (searchStrategyProperty.get()) {
            case FULL_SEARCH ->
                new FullSearch();
            case CUT_OFF -> {
                var strategy = CutoffStrategy.basedOn(new FullSearch());
                strategy.setCutoff(cutoffLevel.get());
                yield strategy;
            }
            case PROBABILISTIC -> {
                var strategy = ProbabilisticSearch.basedOn(new FullSearch());
                strategy.setMinNumberOfMoves(minNumberOfMoves.get());
                strategy.setPercentageOfMovesToCheck(percentageOfMoves.get());
                yield strategy;
            }
            default ->
                    throw new IllegalArgumentException();
        };

        var gameGeometry = gameGeometryProperty.get();
        game = new XoXGame(logger,
                gameGeometry.boardSize,
                gameGeometry.winningStride,
                searchStrategy);


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

    public void onBoardClicked(int row, int col) {
        try {
            gameMode.performHumanMove(row, col);
        }
        catch (Exception e) {
            dialogs.info(e.getMessage());
        }

        notifyModelChanged();
    }
}
