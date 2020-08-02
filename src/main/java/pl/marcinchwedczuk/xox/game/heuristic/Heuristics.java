package pl.marcinchwedczuk.xox.game.heuristic;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.Move;

import java.util.Optional;

public interface Heuristics {
    Score score(Board board, Move lastMove);

}
