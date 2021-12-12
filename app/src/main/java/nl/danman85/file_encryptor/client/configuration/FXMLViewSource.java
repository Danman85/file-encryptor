package nl.danman85.file_encryptor.client.configuration;

import nl.danman85.file_encryptor.client.views.Controller;
import nl.danman85.file_encryptor.client.views.file_encryptor.FileEncryptorController;
import nl.danman85.file_encryptor.client.views.file_encryptor.FileLineController;
import nl.danman85.file_encryptor.client.views.main.MainViewController;

public enum FXMLViewSource {

    MAIN_VIEW("/views/MainView.fxml", MainViewController.class),
    FILE_ENCRYPTOR("/views/file_encryptor/FileEncryptor.fxml", FileEncryptorController.class),
    FILE_LINE("/views/file_encryptor/FileLine.fxml",FileLineController.class);

    private final String resourceUrl;

    private final Class<? extends Controller> clazz;

    FXMLViewSource(final String url, final Class<? extends Controller> clazz) {
        this.resourceUrl = url;
        this.clazz = clazz;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public Class<? extends Controller> getClazz() {
        return clazz;
    }
}
