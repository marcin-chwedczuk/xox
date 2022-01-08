package pl.marcinchwedczuk.xox.util;

public interface Logger {
    void debug(String fmt, Object... args);

    default void debug(String message) {
        debug("%s", message);
    }
}
