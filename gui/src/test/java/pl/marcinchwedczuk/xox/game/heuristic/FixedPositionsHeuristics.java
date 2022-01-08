package pl.marcinchwedczuk.xox.game.heuristic;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.BoardPosition;
import pl.marcinchwedczuk.xox.game.Move;

import java.util.Map;

public class FixedPositionsHeuristics implements Heuristics {
    private final Map<BoardPosition, Double> values;

    public FixedPositionsHeuristics(Map<BoardPosition, Double> values) {
        this.values = values;
    }

    @Override
    public Score score(Board board, Move lastMove) {
        BoardMark player = lastMove.mark();
        double playerTotal = 0.0;

        for (Map.Entry<BoardPosition, Double> entry: values.entrySet()) {
            BoardPosition pos = entry.getKey();
            double posValue = entry.getValue();

            if (board.get(pos.row(), pos.col()) == player) {
                playerTotal += posValue;
            }
        }

        return board.countEmpty() == 0
                ? Score.gameEnded(playerTotal)
                : Score.gameOngoing(playerTotal);
    }
}
