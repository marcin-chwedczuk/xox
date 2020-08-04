package pl.marcinchwedczuk.xox.game.heuristic;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.Move;

public interface Heuristics {
    Score score(Board board, Move lastMove);

}
