package pl.marcinchwedczuk.xox.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.control.ToggleGroupValue;
import pl.marcinchwedczuk.xox.Logger;
import pl.marcinchwedczuk.xox.game.Board;

public class MainWindowController {
    private final Logger logger = new Logger() {
        @Override
        public void debug(String fmt, Object... args) {
            String formatted = String.format(fmt, args);
            // Must be thread-safe
            Platform.runLater(() -> {
                debugLogTextArea.appendText(formatted);
                debugLogTextArea.appendText(System.lineSeparator());
            });
        }
    };

    private final Dialogs dialogs = new JfxDialogs();
    private final MainWindowModel model = new MainWindowModel(dialogs, logger);

    @FXML private TextArea debugLogTextArea;

    @FXML private Canvas boardCanvas;
    @FXML private ChoiceBox<GameGeometry> boardSizeCombo;

    private ToggleGroupValue<GameModeType> gameModeToggleGroup = new ToggleGroupValue<>();
    @FXML private RadioButton humanComputerRadio;
    @FXML private RadioButton computerComputerRadio;
    @FXML private RadioButton computerHumanRadio;

    private ToggleGroupValue<SearchStrategyType> searchStrategyToggleGroup = new ToggleGroupValue<>();
    @FXML private RadioButton probabilisticSearchRadio;
    @FXML private RadioButton cutOffRadio;
    @FXML private RadioButton fullSearchRadio;

    @FXML private Label minNumberOfMovesLbl;
    @FXML private Slider minNumberOfMovesSlider;

    @FXML private Label percentageSearchSpaceLabel;
    @FXML private Slider percentageSearchSpaceSlider;

    @FXML private ChoiceBox<Integer> cutOffLevelCombo;

    @FXML private CheckBox emptyFieldsLoseCheck;
    @FXML private CheckBox countAlmostWinsCheck;
    @FXML private CheckBox emptyFieldsWinCheck;

    @FXML private Button nextMoveBtn;
    @FXML private Button redoBtn;
    @FXML private Button resetBtn;

    @FXML private ProgressIndicator progressIndicator;
    @FXML private TabPane tabPane;

    @FXML
    public void initialize() {
        boardSizeCombo.setItems(model.gameGeometries);
        boardSizeCombo.valueProperty().bindBidirectional(model.gameGeometryProperty);

        gameModeToggleGroup.add(computerComputerRadio, GameModeType.COMPUTER_COMPUTER);
        gameModeToggleGroup.add(humanComputerRadio, GameModeType.HUMAN_COMPUTER);
        gameModeToggleGroup.add(computerHumanRadio, GameModeType.COMPUTER_HUMAN);
        gameModeToggleGroup.valueProperty().bindBidirectional(model.gameModeProperty);

        searchStrategyToggleGroup.add(probabilisticSearchRadio, SearchStrategyType.PROBABILISTIC);
        searchStrategyToggleGroup.add(cutOffRadio, SearchStrategyType.CUT_OFF);
        searchStrategyToggleGroup.add(fullSearchRadio, SearchStrategyType.FULL_SEARCH);
        searchStrategyToggleGroup.valueProperty().bindBidirectional(model.searchStrategyProperty);

        minNumberOfMovesSlider.valueProperty().bindBidirectional(model.minNumberOfMoves);
        minNumberOfMovesLbl.textProperty().bind(model.minNumberOfMoves.asString());

        percentageSearchSpaceSlider.valueProperty().bindBidirectional(model.percentageOfMoves);
        percentageSearchSpaceLabel.textProperty().bind(model.percentageOfMoves.asString());

        cutOffLevelCombo.setItems(model.cutoffLevels);
        cutOffLevelCombo.valueProperty().bindBidirectional(model.cutoffLevel);

        emptyFieldsLoseCheck.selectedProperty().bindBidirectional(model.emptyFieldsLoseProperty);
        emptyFieldsWinCheck.selectedProperty().bindBidirectional(model.emptyFieldsWinsProperty);
        countAlmostWinsCheck.selectedProperty().bindBidirectional(model.countAlmostWinsProperty);

        nextMoveBtn.setOnAction(event -> {
            model.nextMove();
        });
        redoBtn.setOnAction(event -> {
            model.redoMove();
        });
        resetBtn.setOnAction(event -> {
            model.reset();
        });

        boardCanvas.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            boardClicked(x, y);
        });

        progressIndicator.progressProperty().bindBidirectional(model.progress);
        progressIndicator.visibleProperty().bind(model.showProgress);
        tabPane.disableProperty().bind(model.inputEnabled.not());

        model.setModelChangedListener(this::draw);
        draw();
    }

    @FXML private void clearLogs() {
        debugLogTextArea.setText("");
    }

    private void draw() {
        final int LINE_WIDTH = 4;
        final int MARK_LINE_WIDTH = 16;
        final int SPACING = 4;
        final Color colorX = Color.BLACK;
        final Color colorO = Color.RED;
        final Color colorBg = Color.rgb(242, 242, 242);
        final Color colorLine = Color.GRAY;

        Board board = model.board();
        int rows = board.sideSize();
        double width = boardCanvas.getWidth();
        double height = boardCanvas.getHeight();

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
                double xStart = (width / rows) * c + LINE_WIDTH/2.0;
                double yStart = (height / rows) * r + LINE_WIDTH/2.0;

                double xEnd = (width / rows) * (c + 1) - LINE_WIDTH/2.0;
                double yEnd = (height / rows) * (r + 1) - LINE_WIDTH/2.0;


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

    private void boardClicked(double x, double y) {
        int rows = model.board().sideSize();
        double cellWidth = boardCanvas.getWidth() / rows;
        double cellHeight = boardCanvas.getHeight() / rows;

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
            model.onBoardClicked(rr, cc);
        }
    }





}
