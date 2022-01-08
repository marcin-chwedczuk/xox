package pl.marcinchwedczuk.xox.util;

public record ErrorMessage(String message) {
    public static ErrorMessage of(String message) {
        return new ErrorMessage(message);
    }

    @Override
    public String toString() {
        return message;
    }
}
