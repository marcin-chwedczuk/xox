package pl.marcinchwedczuk.xox.game.search;

import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

import java.util.ArrayList;
import java.util.List;

public class FullSearch implements SearchStrategy {

    @Override
    public List<MoveProposal> movesToCheck(Board board, BoardMark player, int level) {
        var moves = new ArrayList<MoveProposal>(board.numberOfFields());

        for (int r = 0; r < board.sideSize(); r++) {
            for (int c = 0; c < board.sideSize(); c++) {
                if (board.isEmpty(r, c)) {
                    moves.add(new MoveProposal(r, c));
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return "FullSearch";
    }
}
