package pl.marcinchwedczuk.xox.game.heuristic;

import org.junit.Test;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.XoXGameRules;

import static org.junit.Assert.*;
import static pl.marcinchwedczuk.xox.game.BoardMark.EMPTY;
import static pl.marcinchwedczuk.xox.game.BoardMark.X;

public class RationalPlayerHeuristicsTest {
    private final BoardMark E = EMPTY;

    private final RationalPlayerHeuristics heuristics_3x3_3 =
            new RationalPlayerHeuristics(new XoXGameRules(3, 3));

    @Test public void countAlmostWins_works() {
        var board = Board.of(
                X, E, X,
                E, X, E,
                E, E, E
        );

        int wins = heuristics_3x3_3.countAlmostWins(board, X);

        // first row and two diagonals
        assertEquals(3, wins);
    }
}