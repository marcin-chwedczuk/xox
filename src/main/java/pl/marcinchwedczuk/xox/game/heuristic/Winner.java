package pl.marcinchwedczuk.xox.game.heuristic;

import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.WinningStride;

import java.util.List;

public class Winner {
    public final BoardMark winner;
    public final List<WinningStride> winningStrides;

    public Winner(BoardMark winner, List<WinningStride> winningStrides) {
        this.winner = winner;
        this.winningStrides = winningStrides;
    }
}
