package pl.marcinchwedczuk.xox.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(
            getClass().getResource("/pl/marcinchwedczuk/xox.gui/MainWindow.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("XOX MiniMax");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
