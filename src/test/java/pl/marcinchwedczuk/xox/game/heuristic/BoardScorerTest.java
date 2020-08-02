package pl.marcinchwedczuk.xox.game.heuristic;

import org.junit.Test;
import pl.marcinchwedczuk.xox.game.*;

import static org.junit.Assert.*;
import static pl.marcinchwedczuk.xox.game.BoardMark.*;

public class BoardScorerTest {
    private final BoardMark E = EMPTY;

    private final BoardScorer scorer_3x3_3 = new BoardScorer(3, 3);
    private final BoardScorer scorer_5x5_3 = new BoardScorer(5, 3);

    @Test public void countWinsOnLastMove_no_stride_3x3() {
        var board = Board.of(
                X, X, O,
                O, O, X,
                X, O, X
        );

        for (int row = 0; row < 3; row++)
        for (int col = 0; col < 3; col++) {
            Move lastMove = move(row, col, board.get(row, col));
            assertCountWinsOnLastMoveIs_3x3_3(0, board, lastMove);
        }
    }

    @Test public void countWinsOnLastMove_no_stride_5x5() {
        var board = Board.of(
                X, O, O, X, X,
                O, O, E, O, O,
                X, X, O, X, X,
                X, E, O, E, O,
                O, O, X, O, X
        );

        for (int row = 0; row < 5; row++)
            for (int col = 0; col < 5; col++) {
                if (board.isEmpty(row, col)) continue;

                Move lastMove = move(row, col, board.get(row, col));
                assertCountWinsOnLastMoveIs_5x5_3(0, board, lastMove);
            }
    }

    @Test public void countWinsOnLastMove_horizontal_stride_3x3_3() {
        var board3 = Board.of(
                X, X, X,
                E, O, E,
                E, E, O
        );

        assertCountWinsOnLastMoveIs_3x3_3(1, board3, move(0, 0, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board3, move(0, 1, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board3, move(0, 2, X));
    }

    @Test public void countWinsOnLastMove_horizontal_stride_5x5_3() {
        var board5 = Board.of(
                X, X, X, X, X,
                E, O, E, E, E,
                E, E, O, E, E,
                E, E, E, O, E,
                E, E, E, E, O
        );

        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 0, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 1, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 2, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 3, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 4, X));
    }

    @Test public void countWinsOnLastMove_vertical_stride_3x3_3() {
        var board = Board.of(
                E, O, X,
                O, E, X,
                E, E, X
        );

        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(0, 2, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(1, 2, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(2, 2, X));
    }

    @Test public void countWinsOnLastMove_vertical_stride_5x5_3() {
        var board5 = Board.of(
                O, E, E, E, X,
                E, O, E, E, X,
                E, E, O, E, X,
                E, E, E, O, X,
                E, E, E, E, X
        );

        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 4, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(1, 4, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(2, 4, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(3, 4, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(4, 4, X));
    }

    @Test public void countWinsOnLastMove_backslash_stride_3x3_3() {
        var board = Board.of(
                X, E, E,
                E, X, E,
                E, E, X
        );

        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(0, 0, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(1, 1, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(2, 2, X));
    }

    @Test public void countWinsOnLastMove_backslash_stride_5x5_3() {
        var board5 = Board.of(
                X, E, E, E, O,
                E, X, E, E, O,
                E, E, X, E, O,
                E, E, E, X, O,
                E, E, E, E, X
        );

        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 0, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(1, 1, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(2, 2, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(3, 3, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(4, 4, X));
    }

    @Test public void countWinsOnLastMove_slash_stride_3x3_3() {
        var board = Board.of(
                E, E, X,
                E, X, E,
                X, E, E
        );

        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(0, 2, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(1, 1, X));
        assertCountWinsOnLastMoveIs_3x3_3(1, board, move(2, 0, X));
    }

    @Test public void countWinsOnLastMove_slash_stride_5x5_3() {
        var board5 = Board.of(
                E, E, E, E, X,
                E, E, E, X, O,
                E, E, X, E, O,
                E, X, E, E, O,
                X, E, E, E, O
        );

        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(0, 4, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(1, 3, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(2, 2, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(3, 1, X));
        assertCountWinsOnLastMoveIs_5x5_3(1, board5, move(4, 0, X));
    }


    @Test public void findWinningStrides_horizontal_5x5_3() {
        var board5 = Board.of(
                X, X, X, O, X, // here
                E, E, O, E, O,
                E, E, O, E, O,
                E, X, X, X, O, // here
                E, E, E, E, O
        );

        var strides = scorer_5x5_3.findWinningStrides(board5, X);

        assertEquals(2, strides.size());
        assertEquals(stride(pos(0,0), pos(0, 2)), strides.get(0));
        assertEquals(stride(pos(3,1), pos(3, 3)), strides.get(1));
    }

    @Test public void findWinningStrides_vertical_5x5_3() {
        var board5 = Board.of(
                E, X, E, O, E,
                E, X, E, E, E,
                E, X, E, O, E,
                E, X, E, O, E,
                E, E, E, O, E
        );      // ^ here, overlapping

        var strides = scorer_5x5_3.findWinningStrides(board5, X);

        assertEquals(2, strides.size());
        assertEquals(stride(pos(0,1), pos(2, 1)), strides.get(0));
        assertEquals(stride(pos(1,1), pos(3, 1)), strides.get(1));
    }

    @Test public void findWinningStrides_diag_slash_5x5_3() {
        var board5 = Board.of(
                E, E, E, E, E,
                E, E, E, X, E,
                E, E, X, O, E,
                E, X, E, O, E,
                E, E, E, O, E
        );

        var strides = scorer_5x5_3.findWinningStrides(board5, X);

        assertEquals(1, strides.size());
        assertEquals(stride(pos(1,3), pos(3, 1)), strides.get(0));
    }

    @Test public void findWinningStrides_diag_backslash_5x5_3() {
        var board5 = Board.of(
                E, E, E, E, E,
                E, X, E, E, E,
                E, E, X, O, E,
                E, E, E, X, O,
                E, E, E, O, E
        );

        var strides = scorer_5x5_3.findWinningStrides(board5, X);

        assertEquals(1, strides.size());
        assertEquals(stride(pos(1,1), pos(3, 3)), strides.get(0));
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
                    var score = scorer_3x3_3.score(board, move(row, col, X));
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

        var winner = scorer_3x3_3.getWinner(board);

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

        var winner = scorer_3x3_3.getWinner(board);

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

        var winner = scorer_3x3_3.getWinner(board);

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

        var winner = scorer_3x3_3.getWinner(board);

        assertTrue(winner.isPresent());
        assertEquals(X, winner.get().winner);

        var winningStrides = winner.get().winningStrides;
        assertEquals(1, winningStrides.size());

        var expected = new WinningStride(
                BoardPosition.of(0, 0),
                BoardPosition.of(2, 2));
        assertEquals(expected, winningStrides.get(0));
    }

    private void assertCountWinsOnLastMoveIs_3x3_3(int expected, Board board, Move lastMove) {
        assertEquals(
                expected,
                scorer_3x3_3.countWinsImpl(board, lastMove));
    }

    private void assertCountWinsOnLastMoveIs_5x5_3(int expected, Board board, Move lastMove) {
        assertEquals(
                String.format("Failed on move (%d, %d).", lastMove.row, lastMove.col),
                expected,
                scorer_5x5_3.countWinsImpl(board, lastMove));
    }

    private WinningStride stride(BoardPosition from, BoardPosition to) {
        return new WinningStride(from, to);
    }

    private BoardPosition pos(int row, int col) {
        return BoardPosition.of(row, col);
    }

    private Move move(int row, int col, BoardMark mark) {
        return new Move(row, col, mark, null, 0, 0, 0);
    }
}