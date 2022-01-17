package pl.marcinchwedczuk.xox.util;

import java.util.function.Consumer;

public sealed interface Either<L,R> permits Left, Right {
    default Either<L,R> onLeft(Consumer<L> consumer) { return this; }
    default Either<L,R> onRight(Consumer<R> consumer) { return this; }

    static <L,R> Either<L,R> left(L value) {
        return new Left<>(value);
    }
    static <L,R> Either<L,R> right(R value) {
        return new Right<>(value);
    }
}
