package pl.marcinchwedczuk.xox.game;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardPosition that = (BoardPosition) o;
        return row == that.row &&
                col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return String.format("row: %d, col: %d", row, col);
    }
}
