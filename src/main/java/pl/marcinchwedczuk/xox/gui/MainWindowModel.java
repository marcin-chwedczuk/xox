package pl.marcinchwedczuk.xox.gui;

import pl.marcinchwedczuk.xox.game.Board;

public class MainWindowModel {
    public Board board;

    public void gameModeChanged(GameMode gameMode) {

    }

    public void boardSizeChanged(int boardSize, int winningStride) {

    }

    public void searchStrategyChanged(SearchStrategyType type) {
    }

    public void heuristicSettingsChanged(boolean emptyFieldsLose, boolean emptyFieldsWins, boolean countAlmostWins) {

    }
}
