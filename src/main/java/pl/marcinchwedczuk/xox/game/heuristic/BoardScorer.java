package pl.marcinchwedczuk.xox.game.heuristic;

import com.google.common.annotations.VisibleForTesting;
import pl.marcinchwedczuk.xox.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pl.marcinchwedczuk.xox.game.BoardMark.EMPTY;

public class BoardScorer {
    public final int boardSize;
    public final int winningStride;

    private boolean countEmptyFieldsOnWin = false;
    private boolean countEmptyFieldsOnLoose = false;
    private boolean countAlmostWins = false;

    public BoardScorer(int boardSize, int winningStride) {
        if (winningStride > boardSize)
            throw new IllegalArgumentException("winningStride");

        this.boardSize = boardSize;
        this.winningStride = winningStride;
    }

    public Score scoreUncompleted(Board board, BoardMark player) {
        // TODO: Count "almost" wins
        List<WinningStride> winsPlayer = findWinningStrides(board, player);
        List<WinningStride> winsOpponent = findWinningStrides(board, player.opponent());
        // the longer the game, the better
        int emptyPlaces = board.countEmpty();
        boolean endGame = !winsPlayer.isEmpty() || !winsOpponent.isEmpty() || (emptyPlaces == 0);

        if (winsPlayer.size() > 0) {
            // The faster we win the better
            return Score.gameEnded(winsPlayer.size()*1000 + emptyPlaces);
        }
        else if (winsOpponent.size() > 0) {
            // The slower (more places filled) we loose or draw the better
            return Score.gameEnded(winsOpponent.size()*1000 - emptyPlaces);
        }
        else if (endGame) {
            return Score.draw(0);
        }
        else {
            // Draw or game inconclusive
            return Score.gameOngoing(0);
        }
    }

    public Score score(Board board, Move lastMove) {

        // TODO: Count "almost" wins
        int wins = countWinsImpl(board, lastMove);
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

    public boolean isGameFinished(Board board) {
        return (board.countEmpty() == 0) || getWinner(board).isPresent();
    }

    public Optional<Winner> getWinner(Board board) {
        List<WinningStride> xWinningStrides = findWinningStrides(board, BoardMark.X);
        if (!xWinningStrides.isEmpty()) {
            return Optional.of(new Winner(BoardMark.X, xWinningStrides));
        }

        List<WinningStride> oWinningStrides = findWinningStrides(board, BoardMark.O);
        if (!oWinningStrides.isEmpty()) {
            return Optional.of(new Winner(BoardMark.O, oWinningStrides));
        }

        return Optional.empty();
    }

    @VisibleForTesting
    public int countWinsImpl(Board board, Move lastMove) {
        final int N = board.sideSize();
        final int STRIDE = winningStride;
        int totalWins = 0;

        final int lastMoveRow = lastMove.row;
        final int lastMoveCol = lastMove.col;
        final BoardMark player = lastMove.mark;

        // horizontal
        {
            int counts = -1; // we will count lastMove twice

            // Count to the right
            for (int i = lastMoveCol; i < N; i++) {
               if (board.get(lastMoveRow, i) == player) {
                   counts++;
               }
               else {
                   break;
               }
            }

            // Count to the left
            for (int i = lastMoveCol; i >= 0; i--) {
                if (board.get(lastMoveRow, i) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            totalWins += (counts >= STRIDE) ? (counts - STRIDE + 1) : 0;
        }

        // vertical
        {
            int counts = -1; // we will count lastMove twice

            // Count to the bottom
            for (int i = lastMoveRow; i < N; i++) {
                if (board.get(i, lastMoveCol) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            // Count to the top
            for (int i = lastMoveRow; i >= 0; i--) {
                if (board.get(i, lastMoveCol) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            totalWins += (counts >= STRIDE) ? (counts - STRIDE + 1) : 0;
        }

        // diagonal \
        {
            int counts = -1; // we will count lastMove twice

            // Count to the bottom-right
            for (int r = lastMoveRow, c = lastMoveCol; r < N && c < N; r++, c++) {
                if (board.get(r, c) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            // Count to the top
            for (int r = lastMoveRow, c = lastMoveCol; r >= 0 && c >= 0; r--, c--) {
                if (board.get(r, c) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            totalWins += (counts >= STRIDE) ? (counts - STRIDE + 1) : 0;
        }

        // diagonal /
        {
            int counts = -1; // we will count lastMove twice

            // Count to the bottom-right
            for (int r = lastMoveRow, c = lastMoveCol; r < N && c >= 0; r++, c--) {
                if (board.get(r, c) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            // Count to the top
            for (int r = lastMoveRow, c = lastMoveCol; r >= 0 && c < N; r--, c++) {
                if (board.get(r, c) == player) {
                    counts++;
                }
                else {
                    break;
                }
            }

            totalWins += (counts >= STRIDE) ? (counts - STRIDE + 1) : 0;
        }

        return totalWins;
    }

    /**
     * Returns list of winning strides for a player.
     */
    private List<WinningStride> findWinningStrides(Board board, BoardMark player) {
        List<WinningStride> winningStrides = new ArrayList<>();

        int N = board.sideSize();

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
                    if (count >= winningStride && (lastMark == player)) {
                        winningStrides.add(new WinningStride(
                                BoardPosition.of(r, c - winningStride + 1),
                                BoardPosition.of(r, c)));
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
                    if (count >= winningStride && (lastMark == player)) {
                        winningStrides.add(new WinningStride(
                                BoardPosition.of(r - winningStride + 1, c),
                                BoardPosition.of(r, c)));
                    }
                }
            }
        }

        // Diagnoals \
        for (int r = 0; r <= N - winningStride; r++) {
            for (int c = 0; c <= N - winningStride; c++) {
                int count = 0;
                BoardMark lastMark = EMPTY;
                int rr = r, cc = c;
                for (int k = 0; k < winningStride; k++) {
                    BoardMark tmp = board.get(rr, cc);
                    if (tmp != lastMark) {
                        lastMark = tmp;
                        count = 1;
                    }
                    else {
                        count++;
                        if (count >= winningStride && (lastMark == player)) {
                            winningStrides.add(new WinningStride(
                                    BoardPosition.of(rr - winningStride + 1, cc - winningStride + 1),
                                    BoardPosition.of(rr, cc)));
                        }
                    }
                    rr++; cc++;
                }
            }
        }

        // Diagonals /
        for (int r = 0; r <= N - winningStride; r++) {
            for (int c = winningStride-1; c < N; c++) {
                int count = 0;
                BoardMark lastMark = EMPTY;
                int rr = r, cc = c;
                for (int k = 0; k < winningStride; k++) {
                    BoardMark tmp = board.get(rr, cc);
                    if (tmp != lastMark) {
                        lastMark = tmp;
                        count = 1;
                    }
                    else {
                        count++;
                        if (count >= winningStride && (lastMark == player)) {
                            winningStrides.add(new WinningStride(
                                    BoardPosition.of(rr - winningStride + 1, cc + winningStride - 1),
                                    BoardPosition.of(rr, cc)));
                        }
                    }
                    rr++; cc--;
                }
            }
        }

        return winningStrides;
    }

    public void setCountEmptyFieldsOnWin(boolean countEmptyFieldsOnWin) {
        this.countEmptyFieldsOnWin = countEmptyFieldsOnWin;
    }

    public void setCountEmptyFieldsOnLoose(boolean countEmptyFieldsOnLoose) {
        this.countEmptyFieldsOnLoose = countEmptyFieldsOnLoose;
    }

    public void setCountAlmostWins(boolean countAlmostWins) {
        this.countAlmostWins = countAlmostWins;
    }

    @Override
    public String toString() {
        return String.format(
                "BoardScorer(%d, stride %d, almostWins %s, emptyWin %s, emptyLoose %s)",
                boardSize, winningStride, countAlmostWins, countEmptyFieldsOnWin,
                countEmptyFieldsOnLoose);
    }
}
