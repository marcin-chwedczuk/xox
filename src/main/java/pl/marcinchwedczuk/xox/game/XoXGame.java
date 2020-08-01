package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.heuristic.BoardScorer;
import pl.marcinchwedczuk.xox.game.heuristic.Winner;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.util.CancelOperation;

import java.util.Optional;
import java.util.Stack;

public class XoXGame {
    private final Logger logger;

    private SearchStrategy searchStrategy;
    private BoardScorer scorer;

    private Stack<GameState> undoStack = new Stack<>();
    private GameState state;

    public XoXGame(Logger logger, int boardSize,
                   BoardScorer boardScorer,
                   SearchStrategy searchStrategy) {
        this.logger = logger;

        this.searchStrategy = searchStrategy;
        this.scorer = boardScorer;

        var board = new Board(boardSize);
        var currentPlayer = BoardMark.X;
        this.state = GameState.forRunningGame(board, currentPlayer);
    }

    public void makeAutomaticMove(CancelOperation cancelOperation) {
        checkCanPerformMove();

        var currentPlayer = state.currentPlayer;

        var algo = new AlfaBetaAlgo(logger, state.boardCopy(), scorer,
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

        if (!state.board.isEmpty(row, col)) {
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
        undoStack.push(state);

        var board = state.boardCopy();
        board.putMark(row, col, state.currentPlayer);

        boolean isFinished = scorer.isGameFinished(board);
        GameState newState;
        if (isFinished) {
            Optional<Winner> maybeWinner = scorer.getWinner(board);

            newState = GameState.forFinishedGame(
                    board, maybeWinner);
        }
        else {
            newState = GameState.forRunningGame(
                    board, state.currentPlayer.opponent());
        }

        this.state = newState;
    }

    public Board board() {
        return state.board.copyOf();
    }

    public GameState state() {
        return state;
    }

    public boolean canUndoMove() {
        return !undoStack.isEmpty();
    }

    public void undoMove() {
        if (!canUndoMove()) {
            return;
        }

        this.state = undoStack.pop();
    }

    public BoardMark currentPlayer() {
        return this.state.currentPlayer;
    }

    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public void setBoardScorer(BoardScorer boardScorer) {
        this.scorer = boardScorer;
    }
}
