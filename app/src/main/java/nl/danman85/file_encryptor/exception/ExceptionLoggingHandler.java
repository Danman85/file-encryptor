package nl.danman85.file_encryptor.exception;

import javafx.application.Platform;
import nl.danman85.file_encryptor.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * Handles logging for Exceptions, and kills the JavaFX application
 */
 public class ExceptionLoggingHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public void handle(@Nonnull final Exception e) {
        logAndExitOnException(e);
    }

    private void logAndExitOnException(final Exception e) {
            LOGGER.error("Uncaught Exception encountered, exiting application", e);
            Platform.exit();

    }
}