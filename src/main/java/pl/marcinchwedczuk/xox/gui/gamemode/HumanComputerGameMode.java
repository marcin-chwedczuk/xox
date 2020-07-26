package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.XoXGame;

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
    public void nextMove() {
        if (game.currentPlayer() != humanPlayer) {
            game.makeAutomaticMove();
        }
    }

    @Override
    public void userClickedOnBoard(int row, int col) {
        if (game.currentPlayer() == humanPlayer) {
            game.makeManualMove(row, col);
        }
    }
}
