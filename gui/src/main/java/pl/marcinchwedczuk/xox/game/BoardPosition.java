package pl.marcinchwedczuk.xox.game;

public record BoardPosition(int row, int col) {
    public static BoardPosition of(int row, int col) {
        return new BoardPosition(row, col);
    }
}
