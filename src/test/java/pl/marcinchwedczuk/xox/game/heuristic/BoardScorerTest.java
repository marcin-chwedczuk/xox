package pl.marcinchwedczuk.xox.game.heuristic;

import junit.framework.TestCase;
import org.junit.Test;
import pl.marcinchwedczuk.xox.game.*;

import static org.junit.Assert.*;
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

    @Test public void properly_gets_winner_row() {
        var board = Board.of(
                X, X, X,
                O, O, E,
                E, E, E
        );

        var winner = scorer.getWinner(board);

        assertTrue(winner.isPresent());
        assertEquals(X, winner.get().winner);

        var winningStrides = winner.get().winningStrides;
        assertEquals(1, winningStrides.size());

        var expected = new WinningStride(
                BoardPosition.of(0, 0),
                BoardPosition.of(0, 2));
        assertEquals(expected, winningStrides.get(0));
    }

    @Test public void properly_gets_winner_column() {
        var board = Board.of(
                E, E, X,
                O, O, X,
                E, E, X
        );

        var winner = scorer.getWinner(board);

        assertTrue(winner.isPresent());
        assertEquals(X, winner.get().winner);

        var winningStrides = winner.get().winningStrides;
        assertEquals(1, winningStrides.size());

        var expected = new WinningStride(
                BoardPosition.of(0, 2),
                BoardPosition.of(2, 2));
        assertEquals(expected, winningStrides.get(0));
    }

    @Test public void properly_gets_winner_slash_diag() {
        var board = Board.of(
                O, E, X,
                O, X, E,
                X, E, E
        );

        var winner = scorer.getWinner(board);

        assertTrue(winner.isPresent());
        assertEquals(X, winner.get().winner);

        var winningStrides = winner.get().winningStrides;
        assertEquals(1, winningStrides.size());

        var expected = new WinningStride(
                BoardPosition.of(0, 2),
                BoardPosition.of(2, 0));
        assertEquals(expected, winningStrides.get(0));
    }

    @Test public void properly_gets_winner_backslash_diag() {
        var board = Board.of(
                X, E, E,
                O, X, E,
                O, E, X
        );

        var winner = scorer.getWinner(board);

        assertTrue(winner.isPresent());
        assertEquals(X, winner.get().winner);

        var winningStrides = winner.get().winningStrides;
        assertEquals(1, winningStrides.size());

        var expected = new WinningStride(
                BoardPosition.of(0, 0),
                BoardPosition.of(2, 2));
        assertEquals(expected, winningStrides.get(0));
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