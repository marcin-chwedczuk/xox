package pl.marcinchwedczuk.xox.game;

import java.util.Objects;

public class WinningStride {
    public final BoardPosition from;
    public final BoardPosition to;

    public WinningStride(BoardPosition from, BoardPosition to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WinningStride that = (WinningStride) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "WinningStride{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
