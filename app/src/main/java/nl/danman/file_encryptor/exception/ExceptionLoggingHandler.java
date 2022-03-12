package nl.danman.file_encryptor.exception;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.application.Platform;
import nl.danman.file_encryptor.App;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles logging for Exceptions, and kills the JavaFX application
 */
 public class ExceptionLoggingHandler {

    public static final Logger LOGGER = LogManager.getLogger(App.class);

    public void handle(@NonNull final Exception e) {
        logAndExitOnException(e);
    }

    private void logAndExitOnException(final Exception e) {
            LOGGER.error("Uncaught Exception encountered, exiting application", e);
            Platform.exit();

    }
}