package pl.marcinchwedczuk.xox.util;

/**
 * This class is thread-safe.
 */
public class CancelOperation {
    private volatile boolean isCanceled = false;

    public void cancel() {
        isCanceled = true;
    }

    public void checkCancelled() {
        if (isCanceled) {
            throw new OperationCanceledException();
        }
    }
}
