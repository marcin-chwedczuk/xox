package pl.marcinchwedczuk.xox;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

public class MainWindowController {
    @FXML private TextArea debugLogTextArea;

    @FXML private Canvas boardCanvas;
    @FXML private ChoiceBox<BoardSizeItem> boardSizeCombo;

    @FXML
    public void initialize() {
        var boardSizeItems = FXCollections.observableArrayList(
                new BoardSizeItem(3, 3),
                new BoardSizeItem(4, 3),
                new BoardSizeItem(4, 4),
                new BoardSizeItem(5, 3),
                new BoardSizeItem(5, 4),
                new BoardSizeItem(5, 5)
        );
        boardSizeCombo.setItems(boardSizeItems);
        boardSizeCombo.setValue(boardSizeItems.get(0));
    }

    private void draw(Board board) {
        final int LINE_WIDTH = 4;
        final int SPACING = 3;
        final Color colorX = Color.BLACK;
        final Color colorO = Color.RED;
        final Color colorBg = Color.rgb(242, 242, 242);
        final Color colorLine = Color.GRAY;

        int rows = board.size();
        double width = boardCanvas.getWidth();
        double height = boardCanvas.getHeight();
        debug("canvas width = %f, height = %f", width, height);

        // Clear board
        var gc = boardCanvas.getGraphicsContext2D();
        gc.setFill(colorBg);
        gc.fillRect(0, 0, width, height);

        // Draw lines
        gc.setLineWidth(LINE_WIDTH);
        gc.setFill(colorLine);
        for (int i = 0; i <= rows; i++) {
            // Vertical
            double xCenter = (width / rows) * i;
            double xStart = xCenter - LINE_WIDTH / 2.0;
            gc.fillRect(xStart, 0, LINE_WIDTH, height);

            // Horizontal
            double yCenter = (height / rows) * i;
            double yStart = yCenter - LINE_WIDTH / 2.0;
            gc.fillRect(0, yStart, width, LINE_WIDTH);
        }

    }

    @FXML private void drawBoard() {
        var b = new Board(boardSizeCombo.getValue().boardSize);
        b.set(0, 0, BoardMark.X);
        b.set(1, 1, BoardMark.X);
        b.set(2, 2, BoardMark.X);
        b.set(1, 2, BoardMark.O);
        b.set(2, 1, BoardMark.O);
        b.set(2, 0, BoardMark.O);

        debug("draw area boardSize=" + boardSizeCombo.getValue().boardSize);
        draw(b);
    }

    private void debug(String fmt, Object... args) {
        String formatted = String.format(fmt, args);
        debugLogTextArea.appendText(formatted);
        debugLogTextArea.appendText(System.lineSeparator());
    }

    private static class BoardSizeItem {
        public final int boardSize;
        public final int winningStride;

        public BoardSizeItem(int boardSize, int winningStride) {
            this.boardSize = boardSize;
            this.winningStride = winningStride;
        }

        @Override
        public String toString() {
            return String.format("%dx%d - %d wins", boardSize, boardSize, winningStride);
        }
    }
}
