module pl.marcinchwedczuk.xox.gui {
    requires javafx.controls;
    requires javafx.fxml;

    exports pl.marcinchwedczuk.xox.gui;

    opens pl.marcinchwedczuk.xox.gui;
    opens pl.marcinchwedczuk.xox.gui.controls;
    opens jfxtras.labs.scene.control;
}