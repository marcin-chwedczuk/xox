package pl.marcinchwedczuk.xox.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

public class JfxDialogs implements Dialogs {
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

    @Override
    public void error(String title, String body) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Info");
        alert.setHeaderText(title);
        alert.setContentText(body);

        alert.showAndWait();
    }

    @Override
    public void exception(Throwable ex) {
        // from: https://code.makery.ch/blog/javafx-dialogs-official/
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Unhandled exception");

        var message = ex.getMessage();
        if (message == null || message.isBlank()) {
            message = ex.getClass().getSimpleName();
        }
        alert.setContentText(message);

        var sw = new StringWriter();
        var pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        var exceptionText = sw.toString();

        var label = new Label("The exception stacktrace:");

        var textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        var expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();

    }
}
