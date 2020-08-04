package pl.marcinchwedczuk.xox.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.ToggleGroupValue;
import pl.marcinchwedczuk.xox.gui.controls.GameBoard;
import pl.marcinchwedczuk.xox.gui.gamemode.GameModeType;
import pl.marcinchwedczuk.xox.mvvm.JfxTimer;
import pl.marcinchwedczuk.xox.util.Logger;

public class MainWindowController {
    private final JfxTimer timer = new JfxTimer();

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

    //@FXML private Canvas boardCanvas;
    @FXML private ChoiceBox<GameGeometry> boardSizeCombo;

    private ToggleGroupValue<GameModeType> gameModeToggleGroup = new ToggleGroupValue<>();
    @FXML private RadioButton humanComputerRadio;
    @FXML private RadioButton computerComputerRadio;
    @FXML private RadioButton computerHumanRadio;

    private ToggleGroupValue<StrategyType> searchStrategyToggleGroup = new ToggleGroupValue<>();
    @FXML private RadioButton probabilisticSearchRadio;
    @FXML private RadioButton cutOffSearchRadio;
    @FXML private RadioButton fullSearchRadio;

    @FXML private Label probabilisticSearch_numberOfMovesLabel;
    @FXML private Slider probabilisticSearch_numberOfMovesSlider;
    @FXML private Label probabilisticSearch_cutoffLabel;
    @FXML private Slider probabilisticSearch_cutoffSlider;

    @FXML private ChoiceBox<Integer> cutOffSearch_cutOffLevelCombo;

    @FXML private CheckBox emptyFieldsLoseCheck;
    @FXML private CheckBox countAlmostWinsCheck;
    @FXML private CheckBox emptyFieldsWinCheck;

    @FXML private Button nextMoveBtn;
    @FXML private Button undoBtn;
    @FXML private Button resetBtn;

    @FXML private TabPane mainPane;
    @FXML private Pane waitCurtain;
    @FXML private Button waitCurtain_cancelBtn;

    @FXML private GameBoard gameBoard;

    @FXML
    public void initialize() {
        boardSizeCombo.setItems(model.gameGeometriesProperty);
        boardSizeCombo.valueProperty().bindBidirectional(model.gameGeometryProperty);

        gameModeToggleGroup.add(computerComputerRadio, GameModeType.COMPUTER_COMPUTER);
        gameModeToggleGroup.add(humanComputerRadio, GameModeType.HUMAN_COMPUTER);
        gameModeToggleGroup.add(computerHumanRadio, GameModeType.COMPUTER_HUMAN);
        gameModeToggleGroup.valueProperty().bindBidirectional(model.gameModeProperty);

        searchStrategyToggleGroup.add(probabilisticSearchRadio, StrategyType.PROBABILISTIC);
        searchStrategyToggleGroup.add(cutOffSearchRadio, StrategyType.CUT_OFF);
        searchStrategyToggleGroup.add(fullSearchRadio, StrategyType.FULL_SEARCH);
        searchStrategyToggleGroup.valueProperty()
                .bindBidirectional(model.searchStrategyModel.searchStrategyTypeProperty);

        probabilisticSearch_numberOfMovesSlider.valueProperty()
                .bindBidirectional(model.searchStrategyModel.probabilisticSearch_numberOfMovesProperty);
        probabilisticSearch_numberOfMovesLabel.textProperty()
                .bind(model.searchStrategyModel.probabilisticSearch_numberOfMovesProperty.asString());

        probabilisticSearch_cutoffSlider.valueProperty()
                .bindBidirectional(model.searchStrategyModel.probabilisticSearch_cutoffLevelProperty);
        probabilisticSearch_cutoffLabel.textProperty()
                .bind(model.searchStrategyModel.probabilisticSearch_cutoffLevelProperty.asString());

        cutOffSearch_cutOffLevelCombo.setItems(model.searchStrategyModel.cutoffSearch_cutoffLevelsProperty);
        cutOffSearch_cutOffLevelCombo.valueProperty()
                .bindBidirectional(model.searchStrategyModel.cutoffSearch_cutoffLevelProperty);

        emptyFieldsLoseCheck.selectedProperty()
                .bindBidirectional(model.heuristicsModel.countEmptyFieldsOnLooseProperty);
        emptyFieldsWinCheck.selectedProperty()
                .bindBidirectional(model.heuristicsModel.countEmptyFieldsOnWinProperty);
        countAlmostWinsCheck.selectedProperty()
                .bindBidirectional(model.heuristicsModel.countAlmostWinsProperty);

        nextMoveBtn.setOnAction(event -> {
            model.nextMoveCommand.execute();
        });
        model.nextMoveCommand.isEnabledProperty().addListener((observable, oldValue, newValue) -> {
            nextMoveBtn.setDisable(!newValue);
        });

        undoBtn.setOnAction(event -> {
            model.undo();
        });
        undoBtn.setDisable(true);
        model.canUndo.addListener((observable, oldValue, newValue) -> {
            undoBtn.setDisable(!newValue);
        });

        resetBtn.setOnAction(event -> {
            model.reset();
        });

        mainPane.disableProperty()
                .bind(model.nextMoveCommand.isRunningProperty());

        model.nextMoveCommand.isRunningProperty().addListener((observable, oldValue, newValue) -> {
            // Extract to custom binding
            timer.scheduleAfterMilliseconds(1000, () -> {
                if (model.nextMoveCommand.isRunningProperty().get()) {
                    waitCurtain.visibleProperty().set(true);
                }
            });

            if (!model.nextMoveCommand.isRunningProperty().get()) {
                waitCurtain.visibleProperty().set(false);
            }

            setCursor(newValue ? Cursor.WAIT : Cursor.DEFAULT);
        });
        waitCurtain_cancelBtn.setOnAction(event -> {
            model.nextMoveCommand.cancel();
        });

        model.gameStateProperty.bindBidirectional(gameBoard.gameStateProperty);

        gameBoard.setOnBoardClicked(model::onBoardClicked);

        model.reset();
    }

    @FXML private void clearLogs() {
        debugLogTextArea.setText("");
    }

    private void setCursor(Cursor cursor) {
        resetBtn.getScene().setCursor(cursor);
    }
}
