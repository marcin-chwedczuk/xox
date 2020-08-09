package pl.marcinchwedczuk.xox.game.heuristic;

import com.google.common.annotations.VisibleForTesting;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.Move;
import pl.marcinchwedczuk.xox.game.XoXGameRules;

public class RationalPlayerHeuristics implements Heuristics {
    public final XoXGameRules gameRules;

    private boolean countEmptyFields = false;
    private boolean countAlmostWins = false;

    public RationalPlayerHeuristics(XoXGameRules gameRules) {
        this.gameRules = gameRules;
    }

    public Score score(Board board, Move lastMove) {
        // TODO: Count "almost" wins
        int wins = gameRules.countWinsForLastMove(board, lastMove);

        int emptyPlaces = board.countEmpty();
        boolean endGame = (wins != 0) || (emptyPlaces == 0);

        // Logic behind empty fields counting:
        // The faster we win (larger number of empty fields) the better
        int emptyFieldBonus = countEmptyFields ? emptyPlaces : 0;
        int almostWinsBonus = countAlmostWins
                ? countAlmostWins(board, lastMove.mark)
                : 0;

        if (wins > 0) {
            return Score.gameEnded(
                    wins*1000 + almostWinsBonus*50 + emptyFieldBonus);
        }
        else if (endGame) {
            // Draw - nobody won
            return Score.gameEnded(0);
        }
        else {
            // Game is still ongoing
            return Score.gameOngoing(almostWinsBonus*50);
        }
    }

    @VisibleForTesting
    int countAlmostWins(Board board, BoardMark player) {
        int almostWins = 0;

        for (int row = 0; row < board.sideSize(); row++) {
            for (int col = 0; col < board.sideSize(); col++) {
                if (!board.isEmpty(row, col)) {
                    continue;
                }

                board.putMark(row, col, player);
                almostWins += gameRules.countWinsForLastMove(
                        board, new Move(row, col, player));

                board.removeMark(row, col);
            }
        }

        return almostWins;
    }

    public void setCountEmptyFields(boolean countEmptyFields) {
        this.countEmptyFields = countEmptyFields;
    }

    public void setCountAlmostWins(boolean countAlmostWins) {
        this.countAlmostWins = countAlmostWins;
    }

    @Override
    public String toString() {
        return String.format(
                "BoardScorer(size: %d, stride: %d, almostWins: %s, empty: %s)",
                gameRules.boardSize, gameRules.winningStride,
                countAlmostWins, countEmptyFields);
    }
}
