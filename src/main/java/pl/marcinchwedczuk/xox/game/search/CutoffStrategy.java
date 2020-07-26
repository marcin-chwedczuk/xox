package pl.marcinchwedczuk.xox.game.search;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CutoffStrategy implements SearchStrategy {
    private final SearchStrategy innerStrategy = new FullSearch();
    private int cutoff = 5;

    public void setCutoff(int level) {
        this.cutoff = level;
    }

    @Override
    public List<MoveProposal> movesToCheck(Board board, BoardMark player, int level) {
        if (level >= cutoff) {
            return Collections.emptyList();
        }

        return innerStrategy.movesToCheck(board, player, level);
    }
}
