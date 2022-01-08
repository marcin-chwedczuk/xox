package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.game.heuristic.Heuristics;
import pl.marcinchwedczuk.xox.game.heuristic.RationalPlayerHeuristics;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;
import pl.marcinchwedczuk.xox.util.CancelOperation;
import pl.marcinchwedczuk.xox.util.Logger;

import java.util.Optional;
import java.util.Stack;

public class XoXGame {
    private final Logger logger;
    private final XoXGameRules gameRules;

    private SearchStrategy searchStrategy;
    private Heuristics scorer;

    private Stack<GameState> undoStack = new Stack<>();
    private GameState state;

    public XoXGame(Logger logger, int boardSize, int winningStride,
                   Heuristics heuristics,
                   SearchStrategy searchStrategy) {
        this.logger = logger;

        this.gameRules = new XoXGameRules(boardSize, winningStride);
        this.searchStrategy = searchStrategy;
        this.scorer = heuristics;

        var board = new Board(boardSize);
        var currentPlayer = BoardMark.X;
        this.state = GameState.forRunningGame(board, currentPlayer);
    }

    public void makeAutomaticMove(CancelOperation cancelOperation) {
        checkCanPerformMove();

        var currentPlayer = state.currentPlayer;

        var algo = new AlphaBetaAlgo(logger, state.boardCopy(), scorer,
                searchStrategy);
        var errorOrMove = algo.selectMove(currentPlayer, cancelOperation);

        errorOrMove.onRight(scoredMove -> {
            if (scoredMove.move.mark() != currentPlayer) {
                throw new AssertionError();
            }

            logger.debug(scoredMove.debugInfo.toString());
            performMove(scoredMove.move.row(), scoredMove.move.col());
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

        boolean isFinished = gameRules.isGameFinished(board);
        GameState newState;
        if (isFinished) {
            Optional<Winner> maybeWinner = gameRules.getWinner(board);

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

    public void setBoardScorer(RationalPlayerHeuristics heuristics) {
        this.scorer = heuristics;
    }
}
