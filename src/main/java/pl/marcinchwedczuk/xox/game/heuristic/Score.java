package pl.marcinchwedczuk.xox.game.heuristic;

public class Score {
    public static Score gameEnded(double score) {
        return new Score(true, score);
    }

    public static Score gameOngoing(double score) {
        return new Score(false, score);
    }

    public static Score draw(double score) {
        return new Score(true, score);
    }

    public final boolean gameEnded;
    public final double score;

    public Score(boolean gameEnded, double score) {
        this.gameEnded = gameEnded;
        this.score = score;
    }
}
