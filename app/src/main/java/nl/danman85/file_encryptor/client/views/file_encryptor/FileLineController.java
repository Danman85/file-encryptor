package nl.danman85.file_encryptor.client.views.file_encryptor;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.danman85.file_encryptor.client.views.Controller;

import java.io.File;

public class FileLineController implements Controller {

    @FXML private HBox root;
    @FXML private Label fileLabel;
    @FXML private Button closeButton;

    private File file;

    public FileLineController() {
        // TODO
        /*
        - Implement encrypt
        - Implement decrypt
         */
    }

    public void initialize() {
    }

    @Override
    public Parent getRoot() {
        return this.root;
    }

    public File getFile() {
        return new File(file.getPath());
    }

    public void setFile(final File file) {
        if (this.file == null) {
            this.file = file;
            this.fileLabel.setText(file.getPath());
        }
    }

    public Button getCloseButton() {
        return closeButton;
    }
}
