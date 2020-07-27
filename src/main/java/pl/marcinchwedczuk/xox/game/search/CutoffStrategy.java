package pl.marcinchwedczuk.xox.game.search;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CutoffStrategy implements SearchStrategy {
    public static CutoffStrategy basedOn(SearchStrategy strategy) {
        return new CutoffStrategy(strategy);
    }

    private final SearchStrategy innerStrategy;
    private int cutoff = 5;

    private CutoffStrategy(SearchStrategy innerStrategy) {
        this.innerStrategy = innerStrategy;
    }

    @Override
    public List<MoveProposal> movesToCheck(Board board, BoardMark player, int level) {
        if (level >= cutoff) {
            return Collections.emptyList();
        }

        // TODO: Requires heuristic with almostWins counting enabled
        return innerStrategy.movesToCheck(board, player, level);
    }

    public void setCutoff(int level) {
        if (cutoff <= 0) {
            throw new IllegalArgumentException("level");
        }

        this.cutoff = level;
    }
}
