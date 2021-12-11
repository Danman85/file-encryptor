package nl.danman85.file_encryptor.client.views.file_encryptor;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import nl.danman85.file_encryptor.client.views.Controller;

public class FileEncryptorController implements Controller {

    @FXML private VBox root;

    public FileEncryptorController() {
    }

    public void initialize() {

    }

    @Override
    public Parent getRoot() {
        return root;
    }
}
