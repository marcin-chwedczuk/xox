package pl.marcinchwedczuk.xox.game.util;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.ScoredMove;

public record MoveDebugInfo(int level,
                            ScoredMove next,
                            double score,
                            Board boardCopy,
                            double alpha, double beta,
                            boolean isGameFinished) {

    @Override
    public String toString() {
        var debugInfo = new StringBuilder();
        String NL = System.lineSeparator();

        debugInfo
                .append("-".repeat(30)).append(NL)
                .append(String.format("LEVEL: %d", level)).append(NL)
                .append(String.format("ALPHA: %.2f, BETA: %.2f", alpha, beta)).append(NL)
                .append(String.format("SCORE: %.4f", score)).append(NL)
                .append("IS_GAME_FINISHED: ").append(isGameFinished).append(NL)
                .append("BOARD:").append(NL)
                .append(boardCopy.asText()).append(NL);

        if (next != null) {
            debugInfo.append(next.debugInfo.toString());
        }

        return debugInfo.toString();
    }
}
