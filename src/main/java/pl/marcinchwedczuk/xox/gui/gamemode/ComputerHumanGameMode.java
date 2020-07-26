package pl.marcinchwedczuk.xox.gui.gamemode;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.XoXGame;
import pl.marcinchwedczuk.xox.gui.Dialogs;

public class ComputerHumanGameMode implements GameMode {
    private final BoardMark computerPlayer = BoardMark.X;

    private final Logger logger;
    private final Dialogs dialogs;
    private final XoXGame game;

    public ComputerHumanGameMode(Logger logger, Dialogs dialogs, XoXGame game) {
        this.logger = logger;
        this.dialogs = dialogs;
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
            dialogs.info("Now it's computer turn! Please click next move button!");
        }
    }
}
