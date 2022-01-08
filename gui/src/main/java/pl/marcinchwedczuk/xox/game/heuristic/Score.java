package pl.marcinchwedczuk.xox.game.heuristic;

public class Score {
    public static Score gameEnded(double score) {
        return new Score(true, score);
    }

    public static Score gameOngoing(double score) {
        return new Score(false, score);
    }

    public final boolean isFinished;
    public final double score;

    private Score(boolean isFinished, double score) {
        this.isFinished = isFinished;
        this.score = score;
    }

    public Score negate() {
        return new Score(isFinished, -score);
    }

    @Override
    public String toString() {
        return String.format("(score: %f, isFinished: %s)", score, isFinished);
    }
}
