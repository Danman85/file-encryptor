package nl.danman85.file_encryptor.client.views.file_encryptor;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.danman85.file_encryptor.client.views.Controller;
import nl.danman85.file_encryptor.client.views.dialogs.AlertUtil;
import nl.danman85.file_encryptor.client.views.dialogs.PasswordDialog;
import nl.danman85.file_encryptor.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;
import java.io.File;
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

    private File file;
    private final AESEncryptionService aesEncryptionService;
    private final FileService fileService;

    public FileLineController() {
        this.aesEncryptionService = SERVICE_FACTORY.getAesEncryptionService();
        this.fileService = SERVICE_FACTORY.getFileService();
    }

    public void initialize() {
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

    public File getFile() {
        return new File(file.getPath());
    }

    public void setFile(final File file) {
        if (this.file == null) {
            this.file = file;
            this.fileLabel.setText(file.getName());
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
            final String plainFileContents = this.fileService.readTextFromFile(this.file);
            final SecretKey key = this.aesEncryptionService.getKeyFromPassword(password, SALT);
            final String cipheredText = this.aesEncryptionService.encryptWithPrefixIv(plainFileContents, key);
            this.fileService.writeTextToFile(cipheredText, this.file);
        } catch (ServiceException e) {
            final String warningMessage = "Unable to encrypt contents";
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }

    private void decryptFileContents(final String password) {
        try {
            final String plainFileContents = this.fileService.readTextFromFile(this.file);
            final SecretKey key = this.aesEncryptionService.getKeyFromPassword(password, SALT);
            final String cipheredText = this.aesEncryptionService.decryptWithPrefixIv(plainFileContents, key);
            this.fileService.writeTextToFile(cipheredText, this.file);
        } catch (ServiceException e) {
            final String warningMessage = "Unable to decrypt contents";
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }
}
