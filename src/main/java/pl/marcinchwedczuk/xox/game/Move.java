package pl.marcinchwedczuk.xox.game;

public class Move {
    public final int row;
    public final int col;
    public final BoardMark mark;

    // Debug info:
    public final Move next;

    public final double score;
    public final double alpha;
    public final double beta;

    public String boardTxt;

    public Move(int row, int col, BoardMark mark,
                Move next,
                double score,
                double alpha, double beta) {
        this.row = row;
        this.col = col;
        this.mark = mark;
        this.next = next;
        this.score = score;
        this.alpha = alpha;
        this.beta = beta;
    }
}
