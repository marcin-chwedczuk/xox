package pl.marcinchwedczuk.xox.mvvm;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import pl.marcinchwedczuk.xox.util.*;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class AsyncCommand<R> {
    private final Function<CancelOperation, R> action;

    private final BooleanProperty isRunningProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty isEnabledProperty = new SimpleBooleanProperty();

    private volatile CancelOperation cancelator = null;

    private final ObjectProperty<Either<Exception, R>> resultProperty =
            new SimpleObjectProperty<>();

    public AsyncCommand(Function<CancelOperation, R> action,
                        ObservableValue<Boolean> isEnabled) {
        this.action = action;

        isEnabledProperty.bind(Bindings.createBooleanBinding(
                () -> !isRunningProperty.get() && isEnabled.getValue(),
                isEnabled, isRunningProperty));
    }

    public void execute() {
        if (!isEnabledProperty.get()) {
            throw new IllegalStateException("Command is not enabled!");
        }

        isRunningProperty.setValue(true);

        final var cancelOp = new CancelOperation();
        this.cancelator = cancelOp;

        Task<Void> actionTask = new Task<>() {
            @Override
            protected Void call() {
                Either<Exception, R> result;

                try {
                    result = Either.right(action.apply(cancelOp));
                }
                catch (OperationCanceledException ex) {
                    // TODO: Think about something better
                    result = null;
                }
                catch (Exception ex) {
                    result = Either.left(ex);
                }

                final Either<Exception, R> finalResult = result;
                Platform.runLater(() -> {
                    resultProperty.setValue(finalResult);
                    isRunningProperty.setValue(false);
                });

                return null;
            }
        };

        var t = new Thread(actionTask);
        t.setDaemon(true);
        t.setName("async-command-thread");
        t.start();
    }

    public void cancel() {
        if (cancelator != null) {
            cancelator.cancel();
        }
    }

    public ReadOnlyBooleanProperty isRunningProperty() {
        return isRunningProperty;
    }

    public ReadOnlyBooleanProperty isEnabledProperty() {
        return isEnabledProperty;
    }

    public ReadOnlyObjectProperty<Either<Exception, R>> resultProperty() {
        return resultProperty;
    }
}
