package nl.danman.file_encryptor.client.model;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import nl.danman.file_encryptor.client.ClientException;
import nl.danman.file_encryptor.model.FileWithContent;
import nl.danman.file_encryptor.service.ServiceException;
import nl.danman.file_encryptor.service.ServiceFactory;
import nl.danman.file_encryptor.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.Objects;

public class EncryptionFile {

    private static final Logger LOGGER = LogManager.getLogger(EncryptionFile.class);

    private static final ServiceFactory SERVICE_FACTORY = ServiceFactoryImpl.getInstance();

    private final BooleanProperty isEncryptedProperty = new SimpleBooleanProperty(false);

    private FileWithContent fileWithContent;

    public EncryptionFile(final FileWithContent fileWithContent) {
        this.fileWithContent = fileWithContent;
        determineEncryptionStateForContents();
        LOGGER.debug("Initialized EncryptedFile object, path=" + this.fileWithContent.getUri() +
                ", isEncrypted=" + this.isEncrypted());
    }

    public void determineEncryptionStateForContents() {
        try {
            this.isEncryptedProperty.setValue(SERVICE_FACTORY
                    .getAesEncryptionService().areFileContentsEncrypted(fileWithContent));
        } catch (ServiceException e) {
            LOGGER.warn("Unable to determine encryption state for file=" + fileWithContent.getUri(), e);
        }
    }

    public void encrypt(@NonNull final SecretKey secretKey) throws ClientException {
        try {
            final String text = fileWithContent.content;
            final String encryptedText = SERVICE_FACTORY.getAesEncryptionService().encryptWithPrefixIv(text, secretKey);
            this.fileWithContent = SERVICE_FACTORY.getFileWithContentService().update(fileWithContent.withNewContent(encryptedText));
            this.isEncryptedProperty.setValue(true);
        } catch (ServiceException e) {
            determineEncryptionStateForContents();
            throw new ClientException(e);
        }
    }

    public void decrypt(@NonNull final SecretKey secretKey) throws ClientException {
        try {
            final String encryptedText = fileWithContent.content;
            final String plainText = SERVICE_FACTORY.getAesEncryptionService().decryptWithPrefixIv(encryptedText, secretKey);
            this.fileWithContent = SERVICE_FACTORY.getFileWithContentService().update(fileWithContent.withNewContent(plainText));
            this.isEncryptedProperty.setValue(false);
        } catch (ServiceException e) {
            determineEncryptionStateForContents();
            throw new ClientException(e);
        }
    }

    public File getFile() {
        return new File(fileWithContent.file.getAbsolutePath());
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
        return fileWithContent.equals(that.fileWithContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileWithContent);
    }
}
