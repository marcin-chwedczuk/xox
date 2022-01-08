package pl.marcinchwedczuk.xox.mvvm;

import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class JfxTimer {
    private final Timer timer = new Timer(/*isDaemon:*/ true);

    public void scheduleAfterMilliseconds(long millis, Runnable action) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(action);
            }
        }, millis);
    }
}
