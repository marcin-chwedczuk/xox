package pl.marcinchwedczuk.xox.game;

import org.junit.jupiter.api.Test;
import pl.marcinchwedczuk.xox.game.heuristic.FixedPositionsHeuristics;
import pl.marcinchwedczuk.xox.game.search.FullSearch;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.ConsoleLogger;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static pl.marcinchwedczuk.xox.game.BoardMark.*;

public class AlphaBetaAlgoTest {
    private final BoardMark E = EMPTY;

    @Test public void smoke_test_1() {
        var board2x2 = Board.emptyWithSide(2);

        var heuristics = new FixedPositionsHeuristics(Map.of(
            BoardPosition.of(0, 0), 500.0,
            BoardPosition.of(0, 1), 600.0,
            BoardPosition.of(1, 0), 700.0,
            BoardPosition.of(1, 1), 800.0
        ));

        var alphaBeta = new AlphaBetaAlgo(
            ConsoleLogger.instance, board2x2, heuristics, new FullSearch());

        BoardMark player = X;
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Either<ErrorMessage, ScoredMove> errorOrMove =
                    alphaBeta.selectMove(player, new CancelOperation());

            errorOrMove.onRight(scoredMove -> {
                moves.add(scoredMove.move);
                board2x2.putMark(
                        scoredMove.move.row, scoredMove.move.col,
                        scoredMove.move.mark);
            });
            errorOrMove.onLeft(err -> fail(err.message));

            player = player.opponent();
        }

        assertMove(moves.get(0), 1, 1, X);
        assertMove(moves.get(1), 1, 0, O);
        assertMove(moves.get(2), 0, 1, X);
        assertMove(moves.get(3), 0, 0, O);
    }

    private static void assertMove(Move m, int row, int col, BoardMark player) {
        var failure = String.format("Expected (%d, %d) player %s but got (%d, %d) player %s.",
                row, col, player, m.row, m.col, m.mark);

        assertEquals(row, m.row, failure);
        assertEquals(col, m.col, failure);
        assertEquals(player, m.mark, failure);
    }
}