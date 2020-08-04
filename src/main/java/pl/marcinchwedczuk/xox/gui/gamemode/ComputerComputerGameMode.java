package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.gui.TextResources;
import pl.marcinchwedczuk.xox.util.*;

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
    public String currentPlayer() {
        return String.format("COMPUTER %s", game.currentPlayer());
    }

    @Override
    public BoardMark currentPlayerMark() {
        return game.currentPlayer();
    }

    @Override
    public Either<ErrorMessage, Unit> performComputerMove(CancelOperation cancelOperation) {
        game.makeAutomaticMove(cancelOperation);
        return Either.right(Unit.instance);
    }

    @Override
    public Either<ErrorMessage, Unit> performHumanMove(int row, int col) {
        return Either.left(ErrorMessage.of(TextResources.NO_HUMAN_MOVE_POSSIBLE));
    }
}
