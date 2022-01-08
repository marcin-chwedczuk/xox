package pl.marcinchwedczuk.xox.util;

import java.util.function.Consumer;

public interface Either<L,R> {
    void onLeft(Consumer<L> consumer);
    void onRight(Consumer<R> consumer);

    static <L,R> Either<L,R> left(L value) {
        return new Left<>(value);
    }

    static <L,R> Either<L,R> right(R value) {
        return new Right<>(value);
    }
}
