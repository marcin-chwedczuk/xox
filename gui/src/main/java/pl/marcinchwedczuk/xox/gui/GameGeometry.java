package pl.marcinchwedczuk.xox.gui;

public record GameGeometry(int boardSize, int winningStride) {
    @Override
    public String toString() {
        return "%dx%d board - %d wins".formatted(boardSize, boardSize, winningStride);
    }
}
