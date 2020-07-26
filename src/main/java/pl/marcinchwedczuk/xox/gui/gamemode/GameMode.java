package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.GameResult;

public interface GameMode {
    void init();
    void nextMove();
    void userClickedOnBoard(int row, int col);
}
