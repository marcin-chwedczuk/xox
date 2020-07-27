package pl.marcinchwedczuk.xox.game.heuristic;

import junit.framework.TestCase;
import org.junit.Test;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.Move;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pl.marcinchwedczuk.xox.game.BoardMark.*;

public class BoardScorerTest {
    private final BoardMark E = EMPTY;

    private final BoardScorer scorer = new BoardScorer(3, 3);

    @Test public void scoringOnLastMoveWorks_no_stride() {
        var board = Board.of(
                X, X, E,
                E, E, E,
                E, E, E
        );

        assertScore(0, board, move(0, 0, X));
        assertScore(0, board, move(0, 1, X));
        assertScore(0, board, move(0, 2, X));
    }

    @Test public void scoringOnLastMoveWorks_horizontal_stride() {
        var board = Board.of(
                X, X, X,
                E, E, E,
                E, E, E
        );

        assertScore(1, board, move(0, 0, X));
        assertScore(1, board, move(0, 1, X));
        assertScore(1, board, move(0, 2, X));

        assertScore(0, board, move(2, 2, X));
        assertScore(0, board, move(1, 1, X));
        assertScore(0, board, move(1, 0, X));
    }

    @Test public void scoringOnLastMoveWorks_vertical_stride() {
        var board = Board.of(
                E, E, X,
                E, E, X,
                E, E, X
        );

        assertScore(1, board, move(0, 2, X));
        assertScore(1, board, move(1, 2, X));
        assertScore(1, board, move(2, 2, X));

        assertScore(0, board, move(0, 0, X));
        assertScore(0, board, move(2, 0, X));
        assertScore(0, board, move(1, 1, X));
        assertScore(0, board, move(2, 1, X));
    }

    @Test public void scoringOnLastMoveWorks_backslash_stride() {
        var board = Board.of(
                X, E, E,
                E, X, E,
                E, E, X
        );

        assertScore(1, board, move(0, 0, X));
        assertScore(1, board, move(1, 1, X));
        assertScore(1, board, move(2, 2, X));

        assertScore(0, board, move(0, 1, X));
        assertScore(0, board, move(1, 0, X));
        assertScore(0, board, move(2, 1, X));
        assertScore(0, board, move(0, 2, X));
    }

    @Test public void scoringOnLastMoveWorks_slash_stride() {
        var board = Board.of(
                E, E, X,
                E, X, E,
                X, E, E
        );

        assertScore(1, board, move(0, 2, X));
        assertScore(1, board, move(1, 1, X));
        assertScore(1, board, move(2, 0, X));

        assertScore(0, board, move(0, 0, X));
        assertScore(0, board, move(1, 0, X));
        assertScore(0, board, move(2, 2, X));
    }

    @Test public void scoringOnLastMove_returns_false_when_game_did_not_end() {
        var board = Board.of(
                X, O, E,
                E, E, E,
                E, E, E
        );

        for (int row = 0; row < board.sideSize(); row++) {
            for (int col = 0; col < board.sideSize(); col++) {
                if (board.isEmpty(row, col)) {
                    board.putMark(row, col, X);
                    var score = scorer.score(board, move(row, col, X));
                    assertFalse(score.gameEnded);
                    board.removeMark(row, col);
                }
            }
        }

    }

    private void assertScore(int expected, Board board, Move lastMove) {
        assertEquals(
                expected,
                scorer.countWinsImpl(board, lastMove));
    }

    private Move move(int row, int col, BoardMark mark) {
        return new Move(row, col, mark, null, 0, 0, 0);
    }
}