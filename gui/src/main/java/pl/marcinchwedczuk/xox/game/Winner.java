package pl.marcinchwedczuk.xox.game;

import java.util.List;

public record Winner(BoardMark winner,
                     List<WinningStride> winningStrides) {
}
