package pl.marcinchwedczuk.xox.util;

public class ConsoleLogger implements Logger {
    public static final ConsoleLogger instance = new ConsoleLogger();

    private ConsoleLogger() { }

    @Override
    public void debug(String fmt, Object... args) {
        System.out.println(fmt.formatted(args));
    }
}
