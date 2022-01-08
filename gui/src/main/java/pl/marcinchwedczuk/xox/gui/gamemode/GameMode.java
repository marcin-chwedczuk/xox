package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Unit;

public interface GameMode {
    void init();

    String currentPlayer();
    BoardMark currentPlayerMark();

    Either<ErrorMessage,Unit> performComputerMove(CancelOperation cancelOperation);
    Either<ErrorMessage,Unit> performHumanMove(int row, int col);
}
