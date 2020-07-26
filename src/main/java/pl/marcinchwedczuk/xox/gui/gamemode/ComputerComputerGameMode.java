package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.GameResult;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Unit;

public class ComputerComputerGameMode implements GameMode {
    private final Logger logger;
    private final XoXGame game;

    public ComputerComputerGameMode(Logger logger, XoXGame game) {
        this.logger = logger;
        this.game = game;
    }

    @Override
    public void init() {
        // Do Nothing
    }

    @Override
    public Either<ErrorMessage, Unit> performComputerMove() {
        game.makeAutomaticMove();
        return Either.right(Unit.instance);
    }

    @Override
    public Either<ErrorMessage, Unit> performHumanMove(int row, int col) {
        return Either.left(ErrorMessage.of(
                "In this game mode user input is not accepted."
        ));
    }
}
