package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.util.Logger;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Unit;

public class HumanComputerGameMode implements GameMode {
    private final BoardMark humanPlayer = BoardMark.X;

    private final Logger logger;
    private final XoXGame game;

    public HumanComputerGameMode(Logger logger, XoXGame game) {
        this.logger = logger;
        this.game = game;
    }

    @Override
    public void init() {

    }

    @Override
    public Either<ErrorMessage, Unit> performComputerMove(CancelOperation cancelOperation) {
        if (game.currentPlayer() != humanPlayer) {
            game.makeAutomaticMove(cancelOperation);
            return Either.right(Unit.instance);
        }
        else {
            return Either.left(ErrorMessage.of(
                    "This is human move turn. Please click on the board."
            ));
        }
    }

    @Override
    public Either<ErrorMessage, Unit> performHumanMove(int row, int col) {
        if (game.currentPlayer() == humanPlayer) {
            game.makeManualMove(row, col);
            return Either.right(Unit.instance);
        }
        else {
            return Either.left(ErrorMessage.of(
                    "This is computer move turn."
            ));
        }
    }
}
