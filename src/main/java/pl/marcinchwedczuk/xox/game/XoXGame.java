package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.heuristic.BoardScorer;
import pl.marcinchwedczuk.xox.game.search.FullSearch;

import java.util.Optional;

public class XoXGame {
    private final Logger logger;

    private final Board board;
    private BoardMark currentPlayer;
    private BoardScorer scorer;

    private GameResult gameResult;

    public XoXGame(Logger logger, int boardSize, int winningStride) {
        this.logger = logger;

        this.board = new Board(boardSize);
        this.scorer = new BoardScorer(boardSize, winningStride);
        this.currentPlayer = BoardMark.X;

        this.gameResult = new GameResult(currentPlayer, false, BoardMark.EMPTY);
    }

    public void makeAutomaticMove() {
        checkCanPerformMove();

        var algo = new AlfaBetaAlgo(logger, board.copyOf(), scorer,
                new FullSearch());
        var move = algo.selectMove(currentPlayer);

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
        if (gameResult.gameEnded) {
            throw new RuntimeException("game already ended");
        }
    }

    private void performMove(int row, int col) {
        board.putMark(row, col, currentPlayer);
        currentPlayer = currentPlayer.opposite();

        Optional<BoardMark> maybeWinner = scorer.getWinner(board);
        boolean gameEnded = scorer.gameEnded(board);
        this.gameResult = maybeWinner
                .map(winner -> new GameResult(currentPlayer, true, winner))
                .orElse(new GameResult(currentPlayer, gameEnded, BoardMark.EMPTY));
    }

    public Board board() {
        return board.copyOf();
    }

    public GameResult gameResult() {
        return gameResult;
    }

    public BoardMark currentPlayer() {
        return currentPlayer;
    }
}
