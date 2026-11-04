import java.time.LocalDateTime;

public class ClinicLogger
        implements AutoCloseable {

    public ClinicLogger() {
        System.out.println("[log] opened");
    }

    public void log(String message) {

        System.out.println(
                "[" +
                        LocalDateTime.now() +
                        "] "
                        +
                        message);
    }

    @Override
    public void close() {

        System.out.println(
                "[log] closed");
    }
}