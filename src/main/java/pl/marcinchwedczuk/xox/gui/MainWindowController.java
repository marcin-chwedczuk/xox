package pl.marcinchwedczuk.xox.gui;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;

public class MainWindowController {
    @FXML private TextArea debugLogTextArea;

    @FXML private Canvas boardCanvas;
    @FXML private ChoiceBox<BoardSizeItem> boardSizeCombo;

    private Board board;

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

        boardCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                boardClicked(x, y);
            }
        });

        reset();
    }

    @FXML private void reset() {
        this.board = new Board(boardSizeCombo.getValue().boardSize);
        drawBoard();
    }

    private void draw() {
        final int LINE_WIDTH = 4;
        final int MARK_LINE_WIDTH = 16;
        final int SPACING = 4;
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

        // Draw board
        gc.setLineWidth(MARK_LINE_WIDTH);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < rows; c++) {
                double xStart = (width / rows) * r + LINE_WIDTH/2.0;
                double yStart = (height / rows) * c + LINE_WIDTH/2.0;

                double xEnd = (width / rows) * (r + 1) - LINE_WIDTH/2.0;
                double yEnd = (height / rows) * (c + 1) - LINE_WIDTH/2.0;


                // Clear
                gc.setFill(colorBg);
                gc.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);


                switch (board.get(r, c)) {
                    case X:
                        gc.setStroke(colorX);
                        gc.beginPath();
                        gc.moveTo(xStart + MARK_LINE_WIDTH, yStart + MARK_LINE_WIDTH);
                        gc.lineTo(xEnd - MARK_LINE_WIDTH, yEnd - MARK_LINE_WIDTH);
                        gc.closePath();
                        gc.stroke();
                        gc.beginPath();
                        gc.moveTo(xEnd - MARK_LINE_WIDTH, yStart + MARK_LINE_WIDTH);
                        gc.lineTo(xStart + MARK_LINE_WIDTH, yEnd - MARK_LINE_WIDTH);
                        gc.closePath();
                        gc.stroke();
                        break;
                    case O:
                        gc.setStroke(colorO);
                        gc.strokeOval(
                                xStart + MARK_LINE_WIDTH,
                                yStart + MARK_LINE_WIDTH,
                                xEnd - xStart - 2*MARK_LINE_WIDTH,
                                yEnd - yStart - 2*MARK_LINE_WIDTH);
                        break;
                }

            }
        }
    }

    @FXML private void drawBoard() {
        draw();
    }

    private void boardClicked(double x, double y) {
        int rows = board.size();
        double cellWidth = boardCanvas.getWidth() / rows;
        double cellHeight = boardCanvas.getHeight() / rows;

        int rr = -1, cc = -1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < rows; c++) {
                double xStart = cellWidth*r;
                double yStart = cellHeight*c;
                double xEnd = cellWidth*(r+1) - 1;
                double yEnd = cellHeight*(c+1) - 1;

                if (xStart <= x && x <= xEnd &&
                    yStart <= y && y <= yEnd) {
                    rr = r; cc = c;
                }
            }
        }

        if (rr != -1) {
            /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText(String.format("row=%d col=%d", rr, cc));

            alert.showAndWait();*/
            board.set(rr, cc, BoardMark.O);
            drawBoard();
        }
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
