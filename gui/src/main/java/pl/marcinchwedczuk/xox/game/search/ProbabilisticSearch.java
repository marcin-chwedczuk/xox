package pl.marcinchwedczuk.xox.game.search;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

import java.util.Collections;
import java.util.List;

public class ProbabilisticSearch implements SearchStrategy {
    public static ProbabilisticSearch basedOn(SearchStrategy innerStrategy) {
        return new ProbabilisticSearch(innerStrategy);
    }

    private final CutoffStrategy cutoffStrategy;

    private int numberOfMoves = 17;

    public ProbabilisticSearch(SearchStrategy innerStrategy) {
        this.cutoffStrategy = CutoffStrategy.basedOn(innerStrategy);
        this.setCutoff(Short.MAX_VALUE);
    }

    @Override
    public List<MoveProposal> movesToCheck(Board board, BoardMark player, int level) {
        List<MoveProposal> moves =
                cutoffStrategy.movesToCheck(board, player, level);

        if (moves.size() <= numberOfMoves) {
            return moves;
        }

        Collections.shuffle(moves);
        return moves.subList(0, numberOfMoves);
    }

    public void setNumberOfMoves(int minNumberOfMoves) {
        if (minNumberOfMoves < 0) {
            throw new IllegalArgumentException("minNumberOfMoves");
        }

        this.numberOfMoves = minNumberOfMoves;
    }

    public void setCutoff(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("cutoffLevel");
        }

        // We tread 0 as "no cutoff"
        level = (level == 0) ? Short.MAX_VALUE : level;
        cutoffStrategy.setCutoff(level);
    }

    @Override
    public String toString() {
        return String.format("ProbabilisticSearch(moves=%d, cutoff=%d)",
                numberOfMoves, cutoffStrategy.getCutoff());
    }
}
