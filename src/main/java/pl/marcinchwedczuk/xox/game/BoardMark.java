package pl.marcinchwedczuk.xox.game;

public enum BoardMark {
    X("X") {
        @Override
        public BoardMark opponent() {
            return BoardMark.O;
        }
    },

    O("O") {
        @Override
        public BoardMark opponent() {
            return BoardMark.X;
        }
    },

    EMPTY(" ") {
        @Override
        public BoardMark opponent() {
            return EMPTY;
        }
    };

    private final String text;

    BoardMark(String text) {
        this.text = text;
    }

    public String asText() { return text; }
    public abstract BoardMark opponent();
}
