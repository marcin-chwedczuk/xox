package pl.marcinchwedczuk.xox;

public enum BoardMark {
    X("X"),
    O("O"),
    EMPTY(" ");

    private final String text;

    BoardMark(String text) {
        this.text = text;
    }

    public String asText() { return text; }
}
