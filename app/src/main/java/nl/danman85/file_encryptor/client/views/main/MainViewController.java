package nl.danman85.file_encryptor.client.views.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import nl.danman85.file_encryptor.client.ClientException;
import nl.danman85.file_encryptor.client.views.Controller;
import nl.danman85.file_encryptor.client.views.ViewPairFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainViewController implements Controller {

    private static final Logger LOGGER = LogManager.getLogger(ViewPairFactory.class);

    private static final ViewPairFactory viewPairFactory = ViewPairFactory.getInstance();

    @FXML private BorderPane root;

    public MainViewController() {
    }

    public void initialize() throws ClientException {
        this.root.setCenter(viewPairFactory.getFileEncryptorViewPair().getRoot());
    }

    @Override
    public BorderPane getRoot() {
        return root;
    }

    public void exitApplication() {
        Platform.exit();
    }
}
