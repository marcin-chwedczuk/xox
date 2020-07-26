package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.GameResult;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Unit;

public interface GameMode {
    void init();

    Either<ErrorMessage,Unit> performComputerMove();
    Either<ErrorMessage,Unit> performHumanMove(int row, int col);
}
