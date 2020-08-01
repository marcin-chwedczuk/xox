package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.heuristic.BoardScorer;
import pl.marcinchwedczuk.xox.game.heuristic.Winner;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.util.CancelOperation;

import java.util.Optional;

public class XoXGame {
    private final Logger logger;

    private final Board board;
    private BoardMark currentPlayer;

    private SearchStrategy searchStrategy;
    private BoardScorer scorer;

    private GameState state;

    public XoXGame(Logger logger, int boardSize, int winningStride,
                   BoardScorer boardScorer,
                   SearchStrategy searchStrategy) {
        this.logger = logger;

        this.board = new Board(boardSize);
        this.currentPlayer = BoardMark.X;

        this.searchStrategy = searchStrategy;
        this.scorer = boardScorer;

        this.state = GameState
                .forRunningGame(this.board.copyOf(), this.currentPlayer);
    }

    public void makeAutomaticMove(CancelOperation cancelOperation) {
        checkCanPerformMove();

        var algo = new AlfaBetaAlgo(logger, board.copyOf(), scorer,
                searchStrategy);
        var errorOrMove = algo.selectMove(currentPlayer, cancelOperation);

        errorOrMove.onRight(move -> {
            if (move.mark != currentPlayer) {
                throw new AssertionError();
            }

            logger.debug("Move ladder");
            var m = move;
            while (m != null) {
                logger.debug("Move (%d, %d) set %s gives %f%n", m.row, m.col, m.mark, m.score);
                // logger.debug("%s", m.boardTxt);
                m = m.next;
            }

            performMove(move.row, move.col);
        });

        errorOrMove.onLeft(error -> {
            logger.debug("%s", error);
        });
    }

    public void makeManualMove(int row, int col) {
        checkCanPerformMove();

        if (!board.isEmpty(row, col)) {
            throw new RuntimeException(String.format(
                    "position (%d, %d) is already taken", row, col));
        }

        performMove(row, col);
    }

    private void checkCanPerformMove() {
        if (state.isFinished) {
            throw new RuntimeException("game already ended");
        }
    }

    private void performMove(int row, int col) {
        board.putMark(row, col, currentPlayer);

        this.currentPlayer = currentPlayer.opponent();

        boolean isFinished = scorer.isGameFinished(board);
        if (isFinished) {
            Optional<Winner> maybeWinner = scorer.getWinner(board);

            this.state = GameState.forFinishedGame(
                    board.copyOf(), maybeWinner);
        }
        else {
            this.state = GameState.forRunningGame(board, currentPlayer);
        }
    }

    public Board board() {
        return board.copyOf();
    }

    public GameState state() {
        return state;
    }

    public BoardMark currentPlayer() {
        return currentPlayer;
    }

    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }
}
