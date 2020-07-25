package pl.marcinchwedczuk.xox.gui;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.AlfaBetaAlgo;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

public class MainWindowModel {
    private final Dialogs dialogs;
    private final Logger logger;

    public Runnable modelChangedListener = () -> { };
    public Board board;

    private BoardMark currentPlayer = BoardMark.X;
    public boolean extraLogging = false;

    public MainWindowModel(Dialogs dialogs, Logger logger) {
        this.dialogs = dialogs;
        this.logger = logger;
    }

    public void setModelChangedListener(Runnable action) {
        modelChangedListener = action;
    }

    private void onModelChanged() {
        modelChangedListener.run();
    }

    public void gameModeChanged(GameMode gameMode) {

    }

    public void boardSizeChanged(int boardSize, int winningStride) {
        board = new Board(boardSize);
        onModelChanged();
    }

    public void searchStrategyChanged(SearchStrategyData type) {
    }

    public void heuristicSettingsChanged(boolean emptyFieldsLose,
                                         boolean emptyFieldsWins,
                                         boolean countAlmostWins) {

    }

    public void nextMove() {
        logger.debug("========== %s =============", currentPlayer);
        var algo = new AlfaBetaAlgo(logger, board.copyOf());
        algo.extraLogging = extraLogging;
        var move = algo.selectMove(currentPlayer);
        board.putMark(move.row, move.col, move.mark);
        currentPlayer = currentPlayer.opposite();

        logger.debug("Move ladder");
        var m = move;
        while (m != null) {
            logger.debug("Move (%d, %d) set %s gives %f%n", m.row, m.col, m.mark, m.score);
            logger.debug("%s", m.boardTxt);
            logger.debug("\n\n\n");
            m = m.next;
        }

        onModelChanged();
    }

    public void redoMove() {

    }

    public void reset() {

    }
}
