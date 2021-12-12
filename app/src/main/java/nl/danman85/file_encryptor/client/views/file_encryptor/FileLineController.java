package nl.danman85.file_encryptor.client.views.file_encryptor;

import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import nl.danman85.file_encryptor.client.views.Controller;

import java.io.File;

public class FileLineController implements Controller {

    @FXML private HBox root;
    private ObservableObjectValue<File> observableObjectValue;

    public FileLineController() {
    }

    @Override
    public Parent getRoot() {
        return this.root;
    }

    private File getObservableObjectValue() {
        return observableObjectValue.get();
    }

    public ObservableObjectValue<File> observableObjectValueProperty() {
        return observableObjectValue;
    }
}
