package pl.marcinchwedczuk.xox.mvvm;

import java.util.concurrent.atomic.AtomicBoolean;

public class Cancelator {
    private AtomicBoolean isCancelled = new AtomicBoolean(false);

    public boolean isCancelled() {
        return isCancelled.get();
    }

    public void throwIfCancelled() {
        if (isCancelled()) {
            throw new OperationCancelledException();
        }
    }

    public void cancel() {
        isCancelled.set(true);
    }
}
