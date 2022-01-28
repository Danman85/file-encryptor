package nl.danman.file_encryptor.client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.danman.file_encryptor.App;
import nl.danman.file_encryptor.client.views.FXMLViewPair;
import nl.danman.file_encryptor.client.views.FXMLViewPairFactory;
import nl.danman.file_encryptor.client.views.main.MainViewController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client is responsible for configuration and starting of the application.
 */
public class Client {

    public static final Logger LOGGER = LogManager.getLogger(App.class);

    private static final FXMLViewPairFactory fxmlViewFactory = FXMLViewPairFactory.getInstance();

    public Client() {
    }

    public void start(final Stage stage) throws Exception {

        LOGGER.info("Starting Client");

        final Parent root = createMainView();

        // Create scene
        final Scene scene =  new Scene(root, 600, 400);

        // Set CSS on Scene

        // Add Scene to Stage and show
        stage.setScene(scene);
        stage.show();
    }

    private Parent createMainView() throws ClientException {
        final FXMLViewPair<MainViewController> viewPair = fxmlViewFactory.getMainViewPair();
        return viewPair.getRoot();
    }
}
