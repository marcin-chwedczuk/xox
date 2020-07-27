package pl.marcinchwedczuk.xox.game.search;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

import java.util.Collections;
import java.util.List;

public class ProbabilisticSearch implements SearchStrategy {
    public static ProbabilisticSearch basedOn(SearchStrategy innerStrategy) {
        return new ProbabilisticSearch(innerStrategy);
    }

    private final SearchStrategy innerStrategy;

    private int minNumberOfMoves = 17;
    private int percentageOfMovesToCheck = 40;

    public ProbabilisticSearch(SearchStrategy innerStrategy) {
        this.innerStrategy = innerStrategy;
    }

    @Override
    public List<MoveProposal> movesToCheck(Board board, BoardMark player, int level) {
        List<MoveProposal> moves =
                innerStrategy.movesToCheck(board, player, level);

        if (moves.size() < minNumberOfMoves) {
            return moves;
        }

        int movesToCheck = Math.max(1, percentageOfMovesToCheck*moves.size()/100);
        Collections.shuffle(moves);
        return moves.subList(0, movesToCheck);
    }

    public void setMinNumberOfMoves(int minNumberOfMoves) {
        if (minNumberOfMoves < 0) {
            throw new IllegalArgumentException("minNumberOfMoves");
        }

        this.minNumberOfMoves = minNumberOfMoves;
    }

    public void setPercentageOfMovesToCheck(int percentageOfMovesToCheck) {
        if (percentageOfMovesToCheck <= 0 || percentageOfMovesToCheck > 100) {
            throw new IllegalArgumentException("percentageOfMovesToCheck");
        }

        this.percentageOfMovesToCheck = percentageOfMovesToCheck;
    }
}
