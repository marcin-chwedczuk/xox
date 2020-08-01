package pl.marcinchwedczuk.xox.game;

public class BoardPosition {
    public static BoardPosition of(int row, int col) {
        return new BoardPosition(row, col);
    }

    public final int row;
    public final int col;

    private BoardPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return String.format("row: %d, col: %d", row, col);
    }
}
