package pl.marcinchwedczuk.xox.game;

import pl.marcinchwedczuk.xox.game.heuristic.Winner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GameState {
    public static GameState forRunningGame(Board board, BoardMark currentPlayer) {
        return new GameState(false,
                board, Optional.of(currentPlayer),
                Optional.empty());
    }

    public static GameState forFinishedGame(Board board, Optional<Winner> winner) {
        return new GameState(true,
                board, Optional.empty(),
                winner);
    }

    public final boolean isFinished;

    public final Board board;
    public final Optional<BoardMark> currentPlayer;
    public final Optional<Winner> winner;

    private GameState(boolean isFinished,
                      Board board,
                      Optional<BoardMark> currentPlayer,
                      Optional<Winner> winner) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.isFinished = isFinished;
        this.winner = winner;
    }
}
