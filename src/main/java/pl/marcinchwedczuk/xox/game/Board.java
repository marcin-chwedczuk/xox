package pl.marcinchwedczuk.xox.game;

import java.util.Arrays;

import static pl.marcinchwedczuk.xox.game.BoardMark.EMPTY;

public class Board {
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
