package pl.marcinchwedczuk.xox.game;

public class GameResult {
    public final BoardMark currentPlayer;
    public final boolean gameEnded;
    public final BoardMark winner;

    public GameResult(BoardMark currentPlayer, boolean gameEnded, BoardMark winner) {
        this.currentPlayer = currentPlayer;
        this.gameEnded = gameEnded;
        this.winner = winner;
    }
}
