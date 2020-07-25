package pl.marcinchwedczuk.xox.gui;

import pl.marcinchwedczuk.xox.game.Board;

public class MainWindowModel {
    private final Dialogs dialogs;

    public Board board;

    public MainWindowModel(Dialogs dialogs) {
        this.dialogs = dialogs;
    }

    public void gameModeChanged(GameMode gameMode) {

    }

    public void boardSizeChanged(int boardSize, int winningStride) {

    }

    public void searchStrategyChanged(SearchStrategyData type) {
    }

    public void heuristicSettingsChanged(boolean emptyFieldsLose, boolean emptyFieldsWins, boolean countAlmostWins) {

    }

    public void nextMove() {

    }

    public void redoMove() {

    }

    public void reset() {

    }
}
