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

    private final int size;
    private final BoardMark[] board;

    public Board(int size) {
        if (size <= 0) throw new IllegalArgumentException("size");

        this.size = size;
        this.board = new BoardMark[size*size];
        Arrays.fill(board, EMPTY);
    }

    public Board copyOf() {
        var b = new Board(size);
        System.arraycopy(board, 0, b.board, 0, board.length);
        return b;
    }

    public int sideSize() { return size; }
    public int numberOfFields() { return size*size; }

    public BoardMark get(int row, int col) {
        return board[row*size + col];
    }

    private void set(int row, int col, BoardMark mark) {
        board[row*size + col] = mark;
    }

    public int countEmpty() {
        int c = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == EMPTY) {
                c++;
            }
        }
        return c;
    }

    public boolean isEmpty(int row, int col) {
        return board[row*size + col] == EMPTY;
    }

    public void putMark(int row, int col, BoardMark mark) {
        board[row*size + col] = mark;
    }

    public void removeMark(int row, int col) {
        board[row*size + col] = EMPTY;
    }

    public boolean isEquivalent(Board other) {
        if (this.size != other.size) {
            return false;
        }

        for (int i = 0; i < size*size; i++) {
            if (board[i] != other.board[i]) {
                return false;
            }
        }

        return true;
    }

    public String asText() {
        StringBuilder text = new StringBuilder();
        String newLine = System.lineSeparator();

        text.append('=')
                .append("===".repeat(size))
                .append(newLine);
        for (int r = 0; r < size; r++) {
            text.append('|');
            for (int c = 0; c < size; c++) {
                text.append(get(r, c).asText().repeat(2))
                    .append('|');
            }
            text.append(newLine)
                    .append('=')
                    .append("===".repeat(size))
                    .append(newLine);
        }
        text.append(newLine);

        return text.toString();
    }
}
