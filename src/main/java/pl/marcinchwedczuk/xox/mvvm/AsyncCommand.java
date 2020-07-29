package pl.marcinchwedczuk.xox.mvvm;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import pl.marcinchwedczuk.xox.util.Either;
import pl.marcinchwedczuk.xox.util.ErrorMessage;
import pl.marcinchwedczuk.xox.util.Unit;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class AsyncCommand<R> {
    private final Function<Cancelator, R> action;

    private final BooleanProperty isRunningProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty isEnabledProperty = new SimpleBooleanProperty();

    private Cancelator cancelator = null;

    private final ObjectProperty<Either<Exception, R>> resultProperty =
            new SimpleObjectProperty<>();

    public AsyncCommand(Function<Cancelator, R> action,
                        ReadOnlyBooleanProperty isEnabled) {
        this.action = action;

        isEnabledProperty.bind(Bindings.createBooleanBinding(
                () -> !isRunningProperty.get() && isEnabled.get(),
                isEnabled, isRunningProperty));
    }

    public void execute() {
        if (!isEnabledProperty.get()) {
            throw new IllegalStateException("Command is not enabled!");
        }

        isRunningProperty.setValue(true);

        final var finalCancellator = new Cancelator();
        this.cancelator = finalCancellator;

        Task<Void> actionTask = new Task<>() {
            @Override
            protected Void call() {
                Either<Exception, R> result;

                try {
                    result = Either.right(action.apply(finalCancellator));
                }
                catch (OperationCancelledException ex) {
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
        cancelator.cancel();
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
