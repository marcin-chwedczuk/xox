package pl.marcinchwedczuk.xox.gui;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.gui.gamemode.ComputerComputerGameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.GameMode;
import pl.marcinchwedczuk.xox.gui.gamemode.HumanComputerGameMode;

public class MainWindowModel {
    private final Dialogs dialogs;
    private final Logger logger;

    private int boardSize;
    private int winningStride;
    private GameModeType gameModeType;

    private XoXGame game;
    private GameMode gameMode;

    public Runnable modelChangedListener = () -> { };

    public MainWindowModel(Dialogs dialogs, Logger logger) {
        this.dialogs = dialogs;
        this.logger = logger;

        this.boardSize = 3;
        this.winningStride = 3;
        this.gameModeType = GameModeType.COMPUTER_COMPUTER;

        reset();
    }

    public void setModelChangedListener(Runnable action) {
        modelChangedListener = action;
    }

    private void notifyModelChanged() {
        modelChangedListener.run();
    }

    public void gameModeChanged(GameModeType gameModeType) {
        this.gameModeType = gameModeType;
        reset();
    }

    public void boardSizeChanged(int boardSize, int winningStride) {
        this.boardSize = boardSize;
        this.winningStride = winningStride;

        reset();
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
        game = new XoXGame(logger, boardSize, winningStride);

        this.gameMode = switch (gameModeType) {
            case COMPUTER_COMPUTER ->
                    new ComputerComputerGameMode(logger, game);
            case HUMAN_COMPUTER ->
                    new HumanComputerGameMode(logger, game);
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
