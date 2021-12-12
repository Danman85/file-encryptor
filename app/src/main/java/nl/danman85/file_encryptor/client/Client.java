package nl.danman85.file_encryptor.client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import nl.danman85.file_encryptor.App;
import nl.danman85.file_encryptor.client.views.ViewPairFactory;
import nl.danman85.file_encryptor.client.views.main.MainViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client is responsible for configuration and starting of the application.
 */
public class Client {

    public static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static final ViewPairFactory fxmlViewFactory = ViewPairFactory.getInstance();

    public Client() {
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

    private Parent createMainView() throws ClientException {
        final Pair<Parent, MainViewController> viewPair = fxmlViewFactory.getMainViewPair();
        return viewPair.getKey();
    }
}
