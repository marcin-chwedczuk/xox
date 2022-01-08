package pl.marcinchwedczuk.xox.game;

public class Move {
    public final int row;
    public final int col;
    public final BoardMark mark;

    public Move(int row, int col, BoardMark mark) {
        this.row = row;
        this.col = col;
        this.mark = mark;
    }
}
