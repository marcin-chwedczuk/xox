package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.game.util.MoveDebugInfo;
import pl.marcinchwedczuk.xox.util.Logger;
import pl.marcinchwedczuk.xox.game.heuristic.Heuristics;
import pl.marcinchwedczuk.xox.game.heuristic.Score;
import pl.marcinchwedczuk.xox.game.search.MoveProposal;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.OperationCanceledException;

import javax.imageio.plugins.bmp.BMPImageWriteParam;
import java.util.List;
import java.util.Optional;

public class AlphaBetaAlgo {
    private final Logger logger;
    private final Board board;
    private final Heuristics scorer;
    private final SearchStrategy searchStrategy;

    public boolean debugMode = true;

    private Board debugBoard = Board.of(
            BoardMark.X, BoardMark.EMPTY, BoardMark.O,
            BoardMark.X, BoardMark.X, BoardMark.EMPTY,
            BoardMark.O, BoardMark.EMPTY, BoardMark.EMPTY);

    public AlphaBetaAlgo(Logger logger,
                         Board board,
                         Heuristics scorer,
                         SearchStrategy searchStrategy) {
        this.logger = logger;
        this.board = board;
        this.scorer = scorer;
        this.searchStrategy = searchStrategy;
    }

    public Either<ErrorMessage, ScoredMove> selectMove(BoardMark mark, CancelOperation cancelOperation) {
        // TODO: ErrorMessage -> Enum
        try {
            var maybeMove = minimax(0, true, mark,
                    Short.MIN_VALUE, Short.MAX_VALUE,
                    cancelOperation);

            return maybeMove
                    .map(Either::<ErrorMessage, ScoredMove>right)
                    .orElse(Either.left(ErrorMessage.of("No move possible!")));
        }
        catch (OperationCanceledException e) {
            return Either.left(ErrorMessage.of("Computation cancelled"));
        }
    }

    public Optional<ScoredMove> minimax(int level, boolean maximize,
                                 BoardMark player,
                                 double alpha, double beta,
                                 CancelOperation cancelOperation) {
        cancelOperation.checkCancelled();

        ScoredMove best = new ScoredMove(
                new Move(-1, -1, BoardMark.EMPTY),
                maximize ? alpha : beta);

        List<MoveProposal> movesToCheck =
                searchStrategy.movesToCheck(board, player, level);
        if (movesToCheck.isEmpty()) {
            return Optional.empty();
        }

        for (MoveProposal move : movesToCheck) {
            // alpha beta pruning
            if ((maximize && (best.score >= beta)) ||
                (!maximize && (best.score <= alpha))) {
                return Optional.of(best);
            }

            // Try move
            board.putMark(move.row, move.col, player);
            var score = scoreBoard(board, move, player, maximize);

            if (board.isEquivalent(debugBoard)) {
                logger.debug("we are there!");
            }

            Optional<ScoredMove> next = Optional.empty();
            if (!score.isFinished) {
                // Game did not end, do opponent move.
                // May return empty() due to e.g. search strategy limits
                next = minimax(level + 1, !maximize, player.opponent(),
                        maximize ? best.score : alpha,
                        maximize ? beta : best.score,
                        cancelOperation);
            }

            var candidate = new ScoredMove(
                    new Move(move.row, move.col, player),
                    next.map(x -> x.score).orElse(score.score));

            if (maximize) {
                best = best.max(candidate);
            } else {
                best = best.min(candidate);
            }

            if (debugMode && (best == candidate)) {
                best.setDebugInfo(new MoveDebugInfo(
                        level, next.orElse(null),
                        best.score,
                        board.copyOf(),
                        alpha, beta,
                        score.isFinished));
            }



            board.removeMark(move.row, move.col);
        }

        return Optional.of(best);
    }

    private Score scoreBoard(Board board,
                             MoveProposal lastMove,
                             BoardMark player,
                             boolean maximize) {
        // Score is in absolute values, 100 - means player
        // wins no matter X or O. 0 means player looses;
        var score = scorer.score(
                board,
                new Move(lastMove.row, lastMove.col, player));

        if (!maximize) {
            return score.negate();
        }

        return score;
    }
}
