package nl.danman.file_encryptor.client.views;

import edu.umd.cs.findbugs.annotations.NonNull;
import nl.danman.file_encryptor.client.views.file_encryptor.FileEncryptorController;
import nl.danman.file_encryptor.client.views.file_encryptor.FileLineController;
import nl.danman.file_encryptor.client.views.main.MainViewController;

public enum FXMLViewDefinition {

    MAIN_VIEW("/views/MainView.fxml", MainViewController.class),
    FILE_ENCRYPTOR("/views/file_encryptor/FileEncryptor.fxml", FileEncryptorController.class),
    FILE_LINE("/views/file_encryptor/FileLine.fxml", FileLineController.class);

    private final String resourceUrl;

    private final Class<? extends Controller> clazz;

    FXMLViewDefinition(final String url, final Class<? extends Controller> clazz) {
        this.resourceUrl = url;
        this.clazz = clazz;
    }

    @NonNull
    public String getResourceUrl() {
        return resourceUrl;
    }

    @NonNull
    public Class<? extends Controller> getClazz() {
        return clazz;
    }
}
