package pl.marcinchwedczuk.xox.game;

public class ScoredMove {
    public final Move move;
    public final double score;

    public Object debugInfo = "No debug info provided.";

    public ScoredMove(Move move, double score) {
        this.move = move;
        this.score = score;
    }

    public ScoredMove max(ScoredMove other) {
        return this.score >= other.score ? this : other;
    }

    public ScoredMove min(ScoredMove other) {
        return this.score <= other.score ? this : other;
    }

    public void setDebugInfo(Object debugInfo) {
        this.debugInfo = debugInfo;
    }
}
