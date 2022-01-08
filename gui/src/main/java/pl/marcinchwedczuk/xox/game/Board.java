package pl.marcinchwedczuk.xox.game;

import java.util.Arrays;

import static pl.marcinchwedczuk.xox.game.BoardMark.EMPTY;

public class Board {
    public static Board emptyWithSide(int size) {
        return new Board(size);
    }

    public static Board of(
            BoardMark a00, BoardMark a01, BoardMark a02,
            BoardMark a10, BoardMark a11, BoardMark a12,
            BoardMark a20, BoardMark a21, BoardMark a22)
    {
        var board = new Board(3);

        board.set(0, 0, a00); board.set(0, 1, a01); board.set(0, 2, a02);
        board.set(1, 0, a10); board.set(1, 1, a11); board.set(1, 2, a12);
        board.set(2, 0, a20); board.set(2, 1, a21); board.set(2, 2, a22);

        return board;
    }

    public static Board of(
            BoardMark a00, BoardMark a01, BoardMark a02, BoardMark a03, BoardMark a04,
            BoardMark a10, BoardMark a11, BoardMark a12, BoardMark a13, BoardMark a14,
            BoardMark a20, BoardMark a21, BoardMark a22, BoardMark a23, BoardMark a24,
            BoardMark a30, BoardMark a31, BoardMark a32, BoardMark a33, BoardMark a34,
            BoardMark a40, BoardMark a41, BoardMark a42, BoardMark a43, BoardMark a44)
    {
        var board = new Board(5);

        board.set(0, 0, a00); board.set(0, 1, a01); board.set(0, 2, a02); board.set(0, 3, a03); board.set(0, 4, a04);
        board.set(1, 0, a10); board.set(1, 1, a11); board.set(1, 2, a12); board.set(1, 3, a13); board.set(1, 4, a14);
        board.set(2, 0, a20); board.set(2, 1, a21); board.set(2, 2, a22); board.set(2, 3, a23); board.set(2, 4, a24);
        board.set(3, 0, a30); board.set(3, 1, a31); board.set(3, 2, a32); board.set(3, 3, a33); board.set(3, 4, a34);
        board.set(4, 0, a40); board.set(4, 1, a41); board.set(4, 2, a42); board.set(4, 3, a43); board.set(4, 4, a44);

        return board;
    }

    private final int sideSize;
    private final BoardMark[] board;

    public Board(int sideSize) {
        if (sideSize <= 0) throw new IllegalArgumentException("size");

        this.sideSize = sideSize;
        this.board = new BoardMark[sideSize * sideSize];

        Arrays.fill(board, EMPTY);
    }

    public Board copyOf() {
        var copy = new Board(sideSize);
        System.arraycopy(board, 0, copy.board, 0, board.length);
        return copy;
    }

    public int sideSize() { return sideSize; }
    public int numberOfFields() { return sideSize * sideSize; }

    public BoardMark get(int row, int col) {
        return board[row* sideSize + col];
    }

    private void set(int row, int col, BoardMark mark) {
        board[row* sideSize + col] = mark;
    }

    public int countEmpty() {
        int count = 0;

        for (BoardMark boardMark : board) {
            if (boardMark == EMPTY) {
                count++;
            }
        }

        return count;
    }

    public boolean isEmpty(int row, int col) {
        return get(row, col) == EMPTY;
    }

    public void putMark(int row, int col, BoardMark mark) {
        set(row, col, mark);
    }

    public void removeMark(int row, int col) {
        set(row, col, EMPTY);
    }

    public String asText() {
        var text = new StringBuilder();
        var newLine = System.lineSeparator();

        text.append('=')
                .append("===".repeat(sideSize))
                .append(newLine);
        for (int r = 0; r < sideSize; r++) {
            text.append('|');
            for (int c = 0; c < sideSize; c++) {
                text.append(get(r, c).asText().repeat(2))
                    .append('|');
            }
            text.append(newLine)
                    .append('=')
                    .append("===".repeat(sideSize))
                    .append(newLine);
        }
        text.append(newLine);

        return text.toString();
    }
}
