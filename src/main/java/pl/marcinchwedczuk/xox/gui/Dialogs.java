package pl.marcinchwedczuk.xox.gui;

public interface Dialogs {
    boolean ask(String question);
    void info(String text);
    void error(String title, String body);
}
