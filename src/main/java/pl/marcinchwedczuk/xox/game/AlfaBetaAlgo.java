package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.heuristic.BoardScorer;
import pl.marcinchwedczuk.xox.game.heuristic.Score;
import pl.marcinchwedczuk.xox.game.search.MoveProposal;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.OperationCanceledException;

import java.util.List;
import java.util.Optional;

public class AlfaBetaAlgo {
    private final Logger logger;
    private final Board board;
    private final BoardScorer scorer;
    private final SearchStrategy searchStrategy;

    public boolean extraLogging = false;

    public AlfaBetaAlgo(Logger logger,
                        Board board,
                        BoardScorer scorer,
                        SearchStrategy searchStrategy) {
        this.logger = logger;
        this.board = board;
        this.scorer = scorer;
        this.searchStrategy = searchStrategy;
    }

    public Either<ErrorMessage, Move> selectMove(BoardMark mark, CancelOperation cancelOperation) {
        // TODO: ErrorMessage -> Enum
        try {
            var maybeMove = minimax(0, true, mark,
                    Short.MIN_VALUE, Short.MAX_VALUE,
                    cancelOperation);

            return maybeMove
                    .map(Either::<ErrorMessage, Move>right)
                    .orElse(Either.left(ErrorMessage.of("No move possible!")));
        }
        catch (OperationCanceledException e) {
            return Either.left(ErrorMessage.of("Computation cancelled"));
        }
    }

    public Optional<Move> minimax(int level, boolean isMaxStep,
                                 BoardMark player,
                                 double alpha, double beta,
                                 CancelOperation cancelOperation) {
        cancelOperation.checkCancelled();

        Move best = new Move(-1, -1,
                player,
                null,
                isMaxStep ? alpha : beta,
                alpha, beta);

        List<MoveProposal> movesToCheck =
                searchStrategy.movesToCheck(board, player, level);

        if (movesToCheck.isEmpty()) {
            return Optional.empty();
        }

        for (MoveProposal move : movesToCheck) {
            // alpha beta pruning
            if (isMaxStep && (best.score >= beta)) {
                return Optional.of(best);
            }
            if (!isMaxStep && (best.score <= alpha)) {
                return Optional.of(best);
            }

            // Try move
            board.putMark(move.row, move.col, player);

            Optional<Move> next = Optional.empty();

            var score = scoreBoard(board, move, player, isMaxStep);


            if (!score.gameEnded) {
                // Game did not end, do opponent move.
                // May return empty() due to e.g. search strategy limits
                next = minimax(level + 1, !isMaxStep, player.opposite(),
                        isMaxStep ? best.score : alpha,
                        isMaxStep ? beta : best.score,
                        cancelOperation);
            }

            var candidate = new Move(move.row, move.col,
                    player,
                    next.orElse(null),
                    next.map(x -> x.score).orElse(score.score),
                    alpha, beta);

            if (isMaxStep) {
                // TODO: Random choice if score the same
                if (candidate.score > best.score) {
                    best = candidate;
                }
            } else {
                if (candidate.score < best.score) {
                    best = candidate;
                }
            }

            board.removeMark(move.row, move.col);
        }

        return Optional.of(best);
    }

    private Score scoreBoard(Board board,
                             MoveProposal lastMove,
                             BoardMark player,
                             boolean isMaxStep) {
        // Score is in absolute values, 100 - means player
        // wins no matter X or O. 0 means player looses;
        var score = scorer.score(board, new Move(lastMove.row, lastMove.col,
                player,
                null,
                Double.NaN, Double.NaN, Double.NaN));

        if (!isMaxStep) {
            return score.negate();
        }

        return score;
    }
}
