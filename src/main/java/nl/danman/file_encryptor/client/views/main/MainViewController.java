package nl.danman.file_encryptor.client.views.main;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import nl.danman.file_encryptor.client.ClientException;
import nl.danman.file_encryptor.client.views.Controller;
import nl.danman.file_encryptor.client.views.FXMLViewPairFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainViewController implements Controller {

    private static final Logger LOGGER = LogManager.getLogger(FXMLViewPairFactory.class);

    private static final FXMLViewPairFactory viewPairFactory = FXMLViewPairFactory.getInstance();

    @FXML private BorderPane root;

    public MainViewController() {
    }

    public void initialize() throws ClientException {
        this.root.setCenter(viewPairFactory.getFileEncryptorViewPair().getRoot());
    }

    @NonNull
    @Override
    public BorderPane getRoot() {
        return root;
    }

    public void exitApplication() {
        Platform.exit();
    }
}
