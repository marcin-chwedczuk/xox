package pl.marcinchwedczuk.xox.util;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
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
