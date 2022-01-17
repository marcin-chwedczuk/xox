package pl.marcinchwedczuk.xox.util;

import java.util.function.Consumer;

record Left<L,R>(L value) implements Either<L,R> {
    @Override
    public Left<L,R> onLeft(Consumer<L> consumer) {
        consumer.accept(value);
        return this;
    }
}
