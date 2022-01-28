package nl.danman.file_encryptor.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AESEncryptionServiceTest {

    private static final String ENCRYPTION_TAG = "<==|| File Encryptor encrypted file ||==>";
    private static final String PASSWORD = "This_is_a_password1";
    private static final String SALT = "iybfiydvcbhvydbv";

    private AESEncryptionService encryptionService;
    private SecretKey defaultKey;

    @BeforeEach
    void setupTest() throws ServiceException {
        this.encryptionService = new AESEncryptionService();
        this.defaultKey = this.encryptionService.getKeyFromPassword(PASSWORD, SALT);
    }

    @Test
    void getKeyFromPassword_generatesKeyWithCorrectEncodedValue() {
        // Given
        final byte[] expectedEncodedValue = new byte[] {
        -122, 95, 41, -120, 69, -110, 99, -103, 93, 98, 76,
        -120, -30, 89, 11, 7, 73, -99, -50, 25, -104, -36,
        94, -88, 14, -76, -65, 16, -11, -94, 88, 117};

        // When
        final byte[] actualEncodedValue = this.defaultKey.getEncoded();

        // Then
        assertArrayEquals(expectedEncodedValue, actualEncodedValue);
    }

    @Test
    void encrypt_generatesAnEncryptedText() throws ServiceException {
        // Given
        final String input = "Text to be encrypted";

        // When
        final String encryptedText = this.encryptionService.encryptWithPrefixIv(input, this.defaultKey);
        final String decryptedText = this.encryptionService.decryptWithPrefixIv(encryptedText, this.defaultKey);

        // Then
        assertEquals(input, decryptedText);
    }

    @Test
    void encrypt_prependsEncryptionTag() throws ServiceException {
        // Given
        final String input = "Text to be encrypted";

        // When
        final String encryptedText = this.encryptionService.encryptWithPrefixIv(input, this.defaultKey);

        // Then
        assertEquals(ENCRYPTION_TAG, encryptedText.substring(0, ENCRYPTION_TAG.length()));
    }

    @Test
    void encrypt_throwsServiceException_onContentsAlreadyEncrypted() throws IOException {
        // Given
        final String encryptedText = getTextFromEnryptedFile();

        // When
        final ServiceException thrown = assertThrows(ServiceException.class,
                () -> this.encryptionService.encryptWithPrefixIv(encryptedText, this.defaultKey));

        // Then
        assertNotNull(thrown);
        assertEquals("Unable to encrypt an already encrypted text", thrown.getMessage());

    }

    @Test
    void decrypt_throwsServiceException_onMissingEncryptionTag() throws IOException {
        // Given
        final String unencryptedText = getTextFromUnencryptedFile();

        // When
        final ServiceException thrown = assertThrows(ServiceException.class,
                () -> this.encryptionService.decryptWithPrefixIv(unencryptedText, this.defaultKey));

        // Then
        assertNotNull(thrown);
        assertEquals("Unable to decrypt an unencrypted text", thrown.getMessage());

    }

    private String getTextFromEnryptedFile() throws IOException {
        final File encryptedFile = new File(Objects.requireNonNull(getClass().getResource("/encryptedTestFile.txt")).getFile());
        return FileUtils.readFileToString(encryptedFile, StandardCharsets.UTF_8);
    }

    private String getTextFromUnencryptedFile() throws IOException {
        final File encryptedFile = new File(Objects.requireNonNull(getClass().getResource("/unencryptedTestFile.txt")).getFile());
        return FileUtils.readFileToString(encryptedFile, StandardCharsets.UTF_8);
    }
}
