package pl.marcinchwedczuk.xox.game.heuristic;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.Move;

import static pl.marcinchwedczuk.xox.game.BoardMark.EMPTY;

public class BoardScorer {
    public int winningStride = 3;

    public Score score(Board board, Move lastMove) {

        // TODO: Count "almost" wins
        int wins = countWins(board, lastMove.mark);
        // the longer the game, the better
        int emptyPlaces = board.countEmpty();
        boolean endGame = (wins != 0) || (emptyPlaces == 0);

        if (wins > 0) {
            // The faster we win the better
            return Score.gameEnded(wins*1000 + emptyPlaces);
        }
        else if (wins < 0) {
            // The slower (more places filled) we loose or draw the better
            return Score.gameEnded(wins*1000 - emptyPlaces);
        }
        else if (endGame) {
            return Score.draw(0);
        }
        else {
            // Draw or game inconclusive
            return Score.gameOngoing(0);
        }
    }

    // Returns number of player winds
    private int countWins(Board board, BoardMark player) {
        // Very crude scoring we ignore situations like
        // X X _ <- empty field

        int N = board.size();
        int WINNING_STRIDE = winningStride;
        int totalWins = 0;

        // Rows
        for (int r = 0; r < N; r++) {
            int count = 0;
            BoardMark lastMark = EMPTY;
            for (int c = 0; c < N; c++) {
                BoardMark tmp = board.get(r, c);
                if (tmp != lastMark) {
                    lastMark = tmp;
                    count = 1;
                }
                else {
                    count++;
                    if (count >= WINNING_STRIDE && (lastMark != EMPTY)) {
                        totalWins += (player == lastMark) ? 1 : -1;
                    }
                }
            }
        }

        // Columns
        for (int c = 0; c < N; c++) {
            int count = 0;
            BoardMark lastMark = EMPTY;
            for (int r = 0; r < N; r++) {
                BoardMark tmp = board.get(r, c);
                if (tmp != lastMark) {
                    lastMark = tmp;
                    count = 1;
                }
                else {
                    count++;
                    if (count >= WINNING_STRIDE && lastMark != EMPTY) {
                        totalWins += (player == lastMark) ? 1 : -1;
                    }
                }
            }
        }

        // Diagnoals \
        for (int r = 0; r <= N - WINNING_STRIDE; r++) {
            for (int c = 0; c <= N - WINNING_STRIDE; c++) {
                int count = 0;
                BoardMark lastMark = EMPTY;
                int rr = r, cc = c;
                for (int k = 0; k < WINNING_STRIDE; k++) {
                    BoardMark tmp = board.get(rr, cc);
                    if (tmp != lastMark) {
                        lastMark = tmp;
                        count = 1;
                    }
                    else {
                        count++;
                        if (count >= WINNING_STRIDE && lastMark != EMPTY) {
                            totalWins += (player == lastMark) ? 1 : -1;
                        }
                    }
                    rr++; cc++;
                }
            }
        }

        // Diagonals /
        for (int r = 0; r <= N - WINNING_STRIDE; r++) {
            for (int c = WINNING_STRIDE-1; c < N; c++) {
                int count = 0;
                BoardMark lastMark = EMPTY;
                int rr = r, cc = c;
                for (int k = 0; k < WINNING_STRIDE; k++) {
                    BoardMark tmp = board.get(rr, cc);
                    if (tmp != lastMark) {
                        lastMark = tmp;
                        count = 1;
                    }
                    else {
                        count++;
                        if (count >= WINNING_STRIDE && lastMark != EMPTY) {
                            totalWins += (player == lastMark) ? 1 : -1;
                        }
                    }
                    rr++; cc--;
                }
            }
        }

        // No-one wins, the less empty places on
        // board the better the game
        return totalWins;
    }
}
