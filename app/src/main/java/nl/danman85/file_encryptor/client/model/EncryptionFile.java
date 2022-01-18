package nl.danman85.file_encryptor.client.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import nl.danman85.file_encryptor.client.ClientException;
import nl.danman85.file_encryptor.service.ServiceException;
import nl.danman85.file_encryptor.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.crypto.SecretKey;
import java.io.File;
import java.util.Objects;

public class EncryptionFile {

    private static final Logger LOGGER = LogManager.getLogger(EncryptionFile.class);

    private final ServiceFactory serviceFactory;

    private final BooleanProperty isEncryptedProperty = new SimpleBooleanProperty(false);

    private final File file;

    public EncryptionFile(final File file, ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.file = file;
        determineEncryptionStateForContents();
        LOGGER.debug("Initialized EncryptedFile object, path=" + this.file.getPath() +
                ", isEncrypted=" + this.isEncrypted());
    }

    public void determineEncryptionStateForContents() {
        try {
            this.isEncryptedProperty.setValue(this.serviceFactory
                    .getAesEncryptionService().areFileContentsEncrypted(file));
        } catch (ServiceException e) {
            LOGGER.warn("Unable to determine encryption state for file=" + file.getName(), e);
        }
    }

    public String read() throws ServiceException {
        determineEncryptionStateForContents();
        return this.serviceFactory.getFileService().read(this.file);
    }

    public void write(@Nonnull final String text) throws ServiceException, ClientException {
        if (isEncrypted()) {
            throw new ClientException("Cannot write to an encrypted file, decrypt it first");
        }
        this.serviceFactory.getFileService().write(text, this.file);
        determineEncryptionStateForContents();
    }

    public void encrypt(@Nonnull final SecretKey secretKey) throws ClientException {
        try {
            final String text = this.serviceFactory.getFileService().read(this.file);
            final String encryptedText = this.serviceFactory.getAesEncryptionService().encryptWithPrefixIv(text, secretKey);
            this.serviceFactory.getFileService().write(encryptedText, this.file);
            this.isEncryptedProperty.setValue(true);
        } catch (ServiceException e) {
            determineEncryptionStateForContents();
            throw new ClientException(e);
        }
    }

    public void decrypt(@Nonnull final SecretKey secretKey) throws ClientException {
        try {
            final String encryptedText = this.serviceFactory.getFileService().read(this.file);
            final String plainText = this.serviceFactory.getAesEncryptionService().decryptWithPrefixIv(encryptedText, secretKey);
            this.serviceFactory.getFileService().write(plainText, this.file);
            this.isEncryptedProperty.setValue(false);
        } catch (ServiceException e) {
            determineEncryptionStateForContents();
            throw new ClientException(e);
        }
    }

    public File getFile() {
        return new File(this.file.getPath());
    }

    public BooleanProperty getIsEncryptedProperty() {
        return this.isEncryptedProperty;
    }

    public boolean isEncrypted() {
        return this.isEncryptedProperty.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EncryptionFile that = (EncryptionFile) o;
        return file.equals(that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }
}
