package pl.marcinchwedczuk.xox.util;

public class ErrorMessage {
    public static ErrorMessage of(String message) {
        return new ErrorMessage(message);
    }

    public final String message;

    private ErrorMessage(String message) {
        this.message = message;
    }
}
