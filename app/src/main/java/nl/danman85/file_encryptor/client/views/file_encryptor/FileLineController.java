package nl.danman85.file_encryptor.client.views.file_encryptor;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.danman85.file_encryptor.client.ClientException;
import nl.danman85.file_encryptor.client.model.EncryptionFile;
import nl.danman85.file_encryptor.client.views.Controller;
import nl.danman85.file_encryptor.client.views.dialogs.AlertUtil;
import nl.danman85.file_encryptor.client.views.dialogs.PasswordDialog;
import nl.danman85.file_encryptor.service.ServiceException;
import nl.danman85.file_encryptor.service.ServiceFactory;
import nl.danman85.file_encryptor.service.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;
import java.util.Optional;

public class FileLineController implements Controller {

    private static final Logger LOGGER = LogManager.getLogger(FileLineController.class);

    private static final ServiceFactory SERVICE_FACTORY = ServiceFactoryImpl.getInstance();
    public static final String SALT = "thisIsASaltValueForWheneverWeHaveMultipleUsers";

    @FXML private HBox root;
    @FXML private Label fileLabel;
    @FXML private Button closeButton;
    @FXML private Button encryptButton;
    @FXML private Button decryptButton;

    private EncryptionFile encryptionFile;

    public FileLineController() {
    }

    public void initialize() {
        // Method called by JavaFX framework
    }

    public void init(@Nonnull final EncryptionFile encryptionFile) {
        setEncryptionFile(encryptionFile);
        this.encryptButton.disableProperty().bind(this.encryptionFile.getIsEncryptedProperty());
        this.decryptButton.disableProperty().bind(this.encryptionFile.getIsEncryptedProperty().not());
    }

    @Override
    public Parent getRoot() {
        return this.root;
    }

    public void encryptFileContents() {
        final String password = showPassWordDialogAndGetPassword();

        if (!password.isBlank()) {
            encryptFileContents(password);
        }
    }

    public void decryptFileContents() {
        final String password = showPassWordDialogAndGetPassword();

        if (!password.isBlank()) {
            decryptFileContents(password);
        }
    }

    public EncryptionFile getFile() {
        return this.encryptionFile;
    }

    public void setEncryptionFile(final EncryptionFile encryptionFile) {
        if (this.encryptionFile == null) {
            this.encryptionFile = encryptionFile;
            this.fileLabel.setText(encryptionFile.getFile().getName());
        }
    }

    public Button getCloseButton() {
        return closeButton;
    }

    private String showPassWordDialogAndGetPassword() {
        final PasswordDialog passwordDialog = new PasswordDialog("Enter password", "Password:");
        final Optional<String> passwordOptional = passwordDialog.showAndWait();
        String password = "";
        if (passwordOptional.isPresent()) {
            password = passwordOptional.get();
        }
        return password;
    }

    private void encryptFileContents(@Nonnull final String password) {
        try {
            final SecretKey key = SERVICE_FACTORY.getAesEncryptionService().getKeyFromPassword(password, SALT);
            this.encryptionFile.encrypt(key);
        } catch (ClientException | ServiceException e) {
            final String warningMessage = "Unable to encrypt contents";
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }

    private void decryptFileContents(final String password) {
        try {
            final SecretKey key = SERVICE_FACTORY.getAesEncryptionService().getKeyFromPassword(password, SALT);
            this.encryptionFile.decrypt(key);
        } catch (ClientException | ServiceException e) {
            final String warningMessage = "Unable to decrypt contents";
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }
}
