package pl.marcinchwedczuk.xox.game;

import org.junit.Test;
import pl.marcinchwedczuk.xox.game.heuristic.FixedPositionsHeuristics;
import pl.marcinchwedczuk.xox.game.search.FullSearch;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.ConsoleLogger;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static pl.marcinchwedczuk.xox.game.BoardMark.*;

public class AlphaBetaAlgoTest {
    private final BoardMark E = EMPTY;

    @Test public void smoke_test() {
        var board = Board.of(
                E, E, E,
                E, E, E,
                E, E, E
        );

        var heuristics = new FixedPositionsHeuristics(Map.of(
            BoardPosition.of(2, 2), 1000.0,
            BoardPosition.of(2, 1), 800.0,
            BoardPosition.of(2, 0), 600.0,
            BoardPosition.of(1, 1), 500.0
        ));

        var alphaBeta = new AlphaBetaAlgo(
            ConsoleLogger.instance, board, heuristics, new FullSearch());

        BoardMark player = X;
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Either<ErrorMessage, ScoredMove> errorOrMove =
                    alphaBeta.selectMove(player, new CancelOperation());

            errorOrMove.onRight(scoredMove -> {
                moves.add(scoredMove.move);
                board.putMark(
                        scoredMove.move.row, scoredMove.move.col,
                        scoredMove.move.mark);
            });
            errorOrMove.onLeft(err -> fail(err.message));

            player = player.opponent();
        }

        assertMove(moves.get(0), 2, 2, X);
        assertMove(moves.get(1), 2, 1, O);
        assertMove(moves.get(2), 2, 0, X);
        assertMove(moves.get(3), 1, 1, O);
    }

    private static void assertMove(Move m, int row, int col, BoardMark player) {
        var failure = String.format("Expected (%d, %d) player %s but got (%d, %d) player %s.",
                row, col, player, m.row, m.col, m.mark);

        assertEquals(failure, row, m.row);
        assertEquals(failure, col, m.col);
        assertEquals(failure, player, m.mark);
    }
}