package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.GameResult;
import pl.marcinchwedczuk.xox.game.XoXGame;

public class ComputerComputerGameMode implements GameMode {
    private final Logger logger;
    private final XoXGame game;

    public ComputerComputerGameMode(Logger logger, XoXGame game) {
        this.logger = logger;
        this.game = game;
    }

    @Override
    public void nextMove() {
        game.makeAutomaticMove();
    }

    @Override
    public void init() {
        // Do Nothing
    }

    @Override
    public void userClickedOnBoard(int row, int col) {
        // Do Nothing
    }
}
