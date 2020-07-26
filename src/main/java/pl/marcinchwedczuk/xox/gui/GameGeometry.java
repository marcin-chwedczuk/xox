package pl.marcinchwedczuk.xox.gui;

public class GameGeometry {
    public final int boardSize;
    public final int winningStride;

    public GameGeometry(int boardSize, int winningStride) {
        this.boardSize = boardSize;
        this.winningStride = winningStride;
    }

    @Override
    public String toString() {
        return String.format("%dx%d board - %d wins", boardSize, boardSize, winningStride);
    }
}
