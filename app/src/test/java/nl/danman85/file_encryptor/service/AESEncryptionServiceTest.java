package nl.danman85.file_encryptor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AESEncryptionServiceTest {

    private AESEncryptionService encryptionService;

    @BeforeEach
    void setupTest() {
        this.encryptionService = new AESEncryptionService();
    }

    @Test
    void encrypt_generatesAnEncryptedText() throws ServiceException {
        // Given
        final String input = "Text to be encrypted";
        final String password = "password";
        final String salt = "iybfiydvcbhvydbv";
        final SecretKey key = this.encryptionService.getKeyFromPassword(password, salt);

        // When
        final String encryptedText = this.encryptionService.encryptWithPrefixIv(input, key);
        final String decryptedText = this.encryptionService.decryptWithPrefixIv(encryptedText, key);

        // Then
        assertEquals(input, decryptedText);
    }
}
