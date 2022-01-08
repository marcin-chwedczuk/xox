package pl.marcinchwedczuk.xox.game.search;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

import java.util.List;

public interface SearchStrategy {
    List<MoveProposal> movesToCheck(Board board,
                                    BoardMark player,
                                    int level);
}
