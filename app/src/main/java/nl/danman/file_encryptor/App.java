/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package nl.danman.file_encryptor;

import javafx.application.Application;
import javafx.stage.Stage;
import nl.danman.file_encryptor.client.Client;
import nl.danman.file_encryptor.exception.ExceptionLoggingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

    public static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        LOGGER.info("Starting Application");
        launch(args);
    }

    @Override
    public void start(final Stage stage) {
        startClient(stage);
    }

    private void startClient(final Stage stage) {
        var exceptionHandler = new ExceptionLoggingHandler();

        try {
            final var client = new Client();
            client.start(stage);
        } catch (final Exception e) {
            exceptionHandler.handle(e);
        }
    }
}
