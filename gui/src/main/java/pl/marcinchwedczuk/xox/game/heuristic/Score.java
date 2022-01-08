package pl.marcinchwedczuk.xox.game.heuristic;

public record Score(boolean isFinished, double score) {
    public static Score gameEnded(double score) {
        return new Score(true, score);
    }

    public static Score gameOngoing(double score) {
        return new Score(false, score);
    }

    public Score negate() {
        return new Score(isFinished, -score);
    }

    @Override
    public String toString() {
        return "(score: %f, isFinished: %s)".formatted(score, isFinished);
    }
}
