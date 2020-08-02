package pl.marcinchwedczuk.xox.game.heuristic;

import pl.marcinchwedczuk.xox.game.*;

public class HeuristicsImpl implements Heuristics {
    public final XoXGameRules gameRules;

    private boolean countEmptyFieldsOnWin = false;
    private boolean countEmptyFieldsOnLoose = false;
    private boolean countAlmostWins = false;

    public HeuristicsImpl(XoXGameRules gameRules) {
        this.gameRules = gameRules;
    }

    public Score score(Board board, Move lastMove) {
        // TODO: Count "almost" wins
        int wins = gameRules.countWinsForLastMove(board, lastMove);
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
            return Score.gameEnded(0);
        }
        else {
            // Draw or game inconclusive
            return Score.gameOngoing(0);
        }
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
                gameRules.boardSize, gameRules.winningStride,
                countAlmostWins, countEmptyFieldsOnWin,
                countEmptyFieldsOnLoose);
    }
}
