package pl.marcinchwedczuk.xox.game;

import java.util.Arrays;

import static pl.marcinchwedczuk.xox.game.BoardMark.EMPTY;

public class Board {
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

    public int size() { return size; }

    public BoardMark get(int row, int col) {
        return board[row*size + col];
    }

    public void set(int row, int col, BoardMark mark) {
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
