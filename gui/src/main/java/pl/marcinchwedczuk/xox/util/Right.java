package pl.marcinchwedczuk.xox.util;

import java.util.function.Consumer;

record Right<L,R>(R value) implements Either<L,R> {
    @Override
    public Right<L,R> onRight(Consumer<R> consumer) {
        consumer.accept(value);
        return this;
    }
}
