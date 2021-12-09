package nl.danman85.file_encryptor.client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.danman85.file_encryptor.App;
import nl.danman85.file_encryptor.client.configuration.FXMLViewSource;
import nl.danman85.file_encryptor.client.views.FXMLLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Client is responsible for codifuration and starting of the application.
 *
 * All exception handling is done here, to prevent it from bubbling up to
 * the JavaFX framework, and to add logging.
 */
public class Client {

    public static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private final FXMLLoaderFactory fxmlLoaderFactory;

    public Client(final FXMLLoaderFactory loaderFactory) {
        this.fxmlLoaderFactory = loaderFactory;
    }

    public void start(final Stage stage) throws Exception {

        LOGGER.info("Starting Client");

        final Parent root = createMainView();

        // Create scene
        final Scene scene =  new Scene(root, 400, 400);

        // Set CSS on Scene

        // Add Scene to Stage and show
        stage.setScene(scene);
        stage.show();
    }

    private Parent createMainView() throws IOException {
        final var MainViewLoader = this.fxmlLoaderFactory.getFXMLLoaderForResource(FXMLViewSource.MAIN_VIEW);
        final Parent root = MainViewLoader.load();
        return root;
    }
}
