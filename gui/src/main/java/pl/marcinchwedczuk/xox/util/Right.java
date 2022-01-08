package pl.marcinchwedczuk.xox.util;

import java.util.function.Consumer;

class Right<L,R> implements Either<L,R> {
    private final R value;

    Right(R value) {
        this.value = value;
    }

    @Override
    public void onLeft(Consumer<L> consumer) {
        // Do nothing
    }

    @Override
    public void onRight(Consumer<R> consumer) {
        consumer.accept(value);
    }
}
