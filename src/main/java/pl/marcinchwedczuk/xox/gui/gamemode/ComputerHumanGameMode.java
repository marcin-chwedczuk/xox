package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.gui.Dialogs;

public class ComputerHumanGameMode implements GameMode {
    private final BoardMark computerPlayer = BoardMark.X;

    private final Logger logger;
    private final XoXGame game;

    public ComputerHumanGameMode(Logger logger, XoXGame game) {
        this.logger = logger;
        this.game = game;
    }

    @Override
    public void init() {

    }

    @Override
    public void nextMove() {
        if (game.currentPlayer() == computerPlayer) {
            game.makeAutomaticMove();
        }

    }

    @Override
    public void userClickedOnBoard(int row, int col) {
        if (game.currentPlayer() != computerPlayer) {
            game.makeManualMove(row, col);
        }
        else {
            // TODO: Return value instead
        }
    }
}
