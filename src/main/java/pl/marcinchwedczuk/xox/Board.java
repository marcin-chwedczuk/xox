package pl.marcinchwedczuk.xox;

import java.util.Arrays;

public class Board {
    private final int size;
    private final BoardMark[] board;

    public Board(int size) {
        if (size <= 0) throw new IllegalArgumentException("size");

        this.size = size;
        this.board = new BoardMark[size*size];
        Arrays.fill(board, BoardMark.EMPTY);
    }

    public int size() { return size; }

    public BoardMark get(int row, int col) {
        return board[row*size + col];
    }

    public void set(int row, int col, BoardMark mark) {
        board[row*size + col] = mark;
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
                    .append("===".repeat(size));
        }
        text.append(newLine);

        return text.toString();
    }
}
