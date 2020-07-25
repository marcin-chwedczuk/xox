package pl.marcinchwedczuk.xox.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import pl.marcinchwedczuk.xox.game.Board;
import pl.marcinchwedczuk.xox.game.BoardMark;
import pl.marcinchwedczuk.xox.game.search.SearchStrategy;

import java.util.Optional;

public class MainWindowController implements Dialogs {
    private final MainWindowModel model = new MainWindowModel(this);

    @FXML private TextArea debugLogTextArea;

    @FXML private Canvas boardCanvas;
    @FXML private ChoiceBox<BoardSizeItem> boardSizeCombo;

    @FXML private ToggleGroup searchSettings;
    @FXML private RadioButton probibalisticSearchRadio;
    @FXML private RadioButton cutOffRadio;
    @FXML private RadioButton fullSearchRadio;

    @FXML private ChoiceBox<Integer> cutOffLevelCombo;

    @FXML private Label minNumberOfMovesLbl;
    @FXML private Slider minNumberOfMovesSlider;

    @FXML private Label percentageSearchSpaceLabel;
    @FXML private Slider percentageSearchSpaceSlider;

    @FXML private CheckBox emptyFieldsLoseCheck;
    @FXML private CheckBox emptyFieldsWinCheck;
    @FXML private CheckBox countAlmostWinsCheck;

    @FXML private Button nextMoveBtn;
    @FXML private Button redoBtn;
    @FXML private Button resetBtn;

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
        boardSizeCombo.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            BoardSizeItem selectedItem = boardSizeItems.get(newValue.intValue());
            model.boardSizeChanged(selectedItem.boardSize, selectedItem.winningStride);
        });
        boardSizeCombo.setValue(boardSizeItems.get(0));

        boardCanvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            boardClicked(x, y);
        });

        cutOffLevelCombo.setItems(FXCollections.observableArrayList(3, 4, 5));
        cutOffLevelCombo.setValue(4);

        minNumberOfMovesSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            minNumberOfMovesLbl.setText(Integer.toString(newValue.intValue()));
        });
        minNumberOfMovesSlider.setValue(17);

        percentageSearchSpaceSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            percentageSearchSpaceLabel.setText(Integer.toString(newValue.intValue()));
        });
        percentageSearchSpaceSlider.setValue(30);

        probibalisticSearchRadio.setUserData(SearchStrategyType.PROBABILISTIC);
        cutOffRadio.setUserData(SearchStrategyType.CUT_OFF);
        fullSearchRadio.setUserData(SearchStrategyType.FULL_SEARCH);
        searchSettings.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            var type = (SearchStrategyType)newValue.getUserData();
            var data = new SearchStrategyData(type,
                    cutOffLevelCombo.getValue(),
                    (int) minNumberOfMovesSlider.getValue(),
                    (int) percentageSearchSpaceSlider.getValue());

            model.searchStrategyChanged(data);
        });

        emptyFieldsLoseCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            onHeuristicPropsChange();
        });
        emptyFieldsWinCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            onHeuristicPropsChange();
        });
        countAlmostWinsCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            onHeuristicPropsChange();
        });

        nextMoveBtn.setOnAction(event -> {
            model.nextMove();
        });
        redoBtn.setOnAction(event -> {
            model.redoMove();
        });
        resetBtn.setOnAction(event -> {
            model.reset();
        });

        reset();
    }

    private void onHeuristicPropsChange() {
        boolean emptyFieldsLose = emptyFieldsLoseCheck.isSelected();
        boolean emptyFieldsWins = emptyFieldsWinCheck.isSelected();
        boolean countAlmostWins = countAlmostWinsCheck.isSelected();

        model.heuristicSettingsChanged(emptyFieldsLose, emptyFieldsWins, countAlmostWins);
    }

    @FXML private void reset() {
        this.model.board = new Board(boardSizeCombo.getValue().boardSize);
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

        int rows = model.board.size();
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


                switch (model.board.get(r, c)) {
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
        int rows = model.board.size();
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
            model.board.set(rr, cc, BoardMark.O);
            drawBoard();
        }
    }

    @FXML private RadioButton humanComputerRadio;
    @FXML private RadioButton computerHumanRadio;
    @FXML private RadioButton computerComputerRadio;

    @FXML private void gameModeChanged() {
        GameMode gameMode = null;
        if (humanComputerRadio.isSelected()) {
            gameMode = GameMode.HUMAN_COMPUTER;
        }
        else if (computerHumanRadio.isSelected()) {
            gameMode = GameMode.COMPUTER_HUMAN;
        }
        else if (computerComputerRadio.isSelected()){
            gameMode = GameMode.COMPUTER_COMPUTER;
        }

        model.gameModeChanged(gameMode);
    }

    private void debug(String fmt, Object... args) {
        String formatted = String.format(fmt, args);
        debugLogTextArea.appendText(formatted);
        debugLogTextArea.appendText(System.lineSeparator());
    }

    @Override
    public boolean ask(String question) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Question");
        alert.setHeaderText(null);
        alert.setContentText(question);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @Override
    public void info(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
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
