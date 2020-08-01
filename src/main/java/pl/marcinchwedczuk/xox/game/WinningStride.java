package pl.marcinchwedczuk.xox.game;

public class WinningStride {
    public final BoardPosition from;
    public final BoardPosition to;

    public WinningStride(BoardPosition from, BoardPosition to) {
        this.from = from;
        this.to = to;
    }
}
