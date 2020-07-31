package pl.marcinchwedczuk.xox.gui.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import pl.marcinchwedczuk.xox.game.Board;

import java.util.function.BiConsumer;

// Based on: https://stackoverflow.com/a/31761362
public class GameBoard extends Pane {
    private final int LINE_WIDTH = 4;
    private final int MARK_LINE_WIDTH = 16;

    private final Color colorX = Color.BLACK;
    private final Color colorO = Color.RED;
    private final Color colorBg = Color.rgb(242, 242, 242);
    private final Color colorLine = Color.GRAY;

    private final Canvas canvas;
    private BiConsumer<Integer, Integer> onBoardClicked = (row, col) -> { };
    public ObjectProperty<Board> boardProperty = new SimpleObjectProperty<>();

    public GameBoard() { this(0, 0); }

    public GameBoard(double width, double height) {
        canvas = new Canvas(width, height);
        getChildren().add(canvas);

        canvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            boardClicked(x, y);
        });

        boardProperty.addListener((observable, oldValue, newValue) -> draw());
    }

    public void setOnBoardClicked(BiConsumer<Integer, Integer> action) {
        this.onBoardClicked = action;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();

        final double w = snapSizeX(getWidth()) - x - snappedRightInset();
        final double h = snapSizeY(getHeight()) - y - snappedBottomInset();

        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);

        draw();
    }

    private void draw() {
        Board board = boardProperty.get();
        if (board == null) {
            return;
        }

        double width = canvas.getWidth();
        double height = canvas.getHeight();
        var gc = canvas.getGraphicsContext2D();

        clearBoard(gc, width, height);
        drawGrid(gc, board, width, height);
        drawMarks(gc, board, width, height);
    }

    private void drawMarks(GraphicsContext gc, Board board, double width, double height) {
        final int rows = board.sideSize();

        gc.setLineWidth(MARK_LINE_WIDTH);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < rows; c++) {
                double xStart = (width / rows) * c + LINE_WIDTH/2.0;
                double yStart = (height / rows) * r + LINE_WIDTH/2.0;

                double xEnd = (width / rows) * (c + 1) - LINE_WIDTH/2.0;
                double yEnd = (height / rows) * (r + 1) - LINE_WIDTH/2.0;

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

    private void clearBoard(GraphicsContext gc, double width, double height) {
        gc.setFill(colorBg);
        gc.fillRect(0, 0, width, height);
    }

    private void drawGrid(GraphicsContext gc, Board board, double width, double height) {
        gc.setLineWidth(LINE_WIDTH);
        gc.setFill(colorLine);

        final int rows = board.sideSize();

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

    private void boardClicked(double x, double y) {
        Board board = boardProperty.get();
        if (board == null) {
            return;
        }

        int rows = board.sideSize();
        double cellWidth = canvas.getWidth() / rows;
        double cellHeight = canvas.getHeight() / rows;

        int rr = -1, cc = -1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < rows; c++) {
                double xStart = cellWidth*c;
                double yStart = cellHeight*r;
                double xEnd = cellWidth*(c+1) - 1;
                double yEnd = cellHeight*(r+1) - 1;

                if (xStart <= x && x <= xEnd &&
                        yStart <= y && y <= yEnd) {
                    rr = r; cc = c;
                }
            }
        }

        if (rr != -1) {
            onBoardClicked.accept(rr, cc);
        }
    }
}
