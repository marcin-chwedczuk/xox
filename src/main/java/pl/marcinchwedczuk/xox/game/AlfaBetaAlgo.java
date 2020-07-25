package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.heuristic.BoardScorer;

public class AlfaBetaAlgo {
    private final Logger logger;
    private final Board board;
    private final BoardScorer scorer = new BoardScorer();

    public boolean extraLogging = false;

    public AlfaBetaAlgo(Logger logger, Board board) {
        this.logger = logger;
        this.board = board;
    }

    public Move selectMove(BoardMark mark) {
        return minimax(0, true, mark, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    public Move minimax(int level, boolean isMaxStep,
                        BoardMark player,
                        double alpha, double beta) {
        Move best = new Move(-1, -1,
                player,
                null,
                isMaxStep ? alpha : beta,
                alpha, beta);

        int N = board.size();

        // For each possible move
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // Prunning
                if (isMaxStep && (best.score >= beta)) {
                    return best;
                }
                if (!isMaxStep && (best.score <= alpha)) {
                    return best;
                }

                if (board.isEmpty(r, c)) {
                    // Try move
                    board.putMark(r, c, player);

                    Move next = null;

                    // Score is in absolute values, 100 - means player
                    // wins no matter X or O. 0 means player looses;
                    var score = scorer.score(board, new Move(r, c,
                            player,
                            null,
                            Double.NaN, Double.NaN, Double.NaN));

                    var scoreI = score.score;
                    if (!score.gameEnded) {
                        // Game did not end, do opponent move
                        next = minimax(level + 1, !isMaxStep, player.opposite(),
                                isMaxStep ? best.score : alpha,
                                isMaxStep ? beta       : best.score
                        );
                        scoreI = next.score;
                    }
                    else {
                        if (!isMaxStep) {
                            scoreI = -scoreI;
                        }
                    }

                    var candidate = new Move(r, c,
                            player,
                            next,
                            scoreI,
                            alpha, beta);

                    if (isMaxStep) {
                        // TODO: Random choice if score the same
                        if (scoreI > best.score) {
                            best = candidate;
                        }
                    } else {
                        if (scoreI < best.score) {
                            best = candidate;
                        }
                    }

                    board.removeMark(r, c);
                }
            }
        }

        return best;
    }
}
