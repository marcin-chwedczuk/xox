package pl.marcinchwedczuk.xox.util;

import java.util.function.Consumer;

class Left<L,R> implements Either<L,R> {
    private final L value;

    Left(L value) {
        this.value = value;
    }

    @Override
    public void onLeft(Consumer<L> consumer) {
        consumer.accept(value);
    }

    @Override
    public void onRight(Consumer<R> consumer) {
        // do nothing
    }
}
