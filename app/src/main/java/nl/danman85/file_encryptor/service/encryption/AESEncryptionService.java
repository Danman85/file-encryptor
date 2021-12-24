package nl.danman85.file_encryptor.service.encryption;

import nl.danman85.file_encryptor.service.ServiceException;
import nl.danman85.file_encryptor.service.ServiceFactory;
import nl.danman85.file_encryptor.service.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESEncryptionService {

    private static final Logger LOGGER = LogManager.getLogger(AESEncryptionService.class);

    private static final ServiceFactory SERVICE_FACTORY = ServiceFactoryImpl.getInstance();

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH = 128;
    private static final int IV_BYTE_LENGTH = 16;
    private static final String ENCRYPTION_TAG = "<==|| File Encryptor encrypted file ||==>";

    @Nonnull
    public SecretKey getKeyFromPassword(@Nonnull final String password,
                                        @Nonnull final String salt)
            throws ServiceException {
        final SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            return new SecretKeySpec(secretKeyFactory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.warn("Unable to create key", e);
            throw new ServiceException(e);
        }
    }

    @Nonnull
    public String encryptWithPrefixIv(@Nonnull final String plainText,
                                      @Nonnull final SecretKey key) throws ServiceException {
        if (areFileContentsEncrypted(plainText)) {
            throw new ServiceException("Unable to encrypt an already encrypted text");
        }
        final byte[] iv = generateIv();
        final byte[] cipheredText;
        try {
            cipheredText = encrypt(plainText.trim(), key, iv);
            final byte[] cipheredTexWithIv = ByteBuffer.allocate(iv.length + cipheredText.length)
                    .put(iv)
                    .put(cipheredText)
                    .array();
            return ENCRYPTION_TAG + Base64.getEncoder().encodeToString(cipheredTexWithIv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.warn("Unable the encrypt text", e);
            throw new ServiceException(e);
        }
    }

    @Nonnull
    public String decryptWithPrefixIv(@Nonnull final String cipheredText,
                                      @Nonnull final SecretKey key) throws ServiceException {
        final String deTaggedCipheredText;
        if (areFileContentsEncrypted(cipheredText)) {
            deTaggedCipheredText = removeEncryptionTagIfPresent(cipheredText.trim());
        } else {
            throw new ServiceException("Unable to decrypt an unencrypted text");
        }

        final ByteBuffer bb = ByteBuffer.wrap(Base64.getDecoder().decode(deTaggedCipheredText));
        final byte[] iv = new byte[IV_BYTE_LENGTH];
        bb.get(iv);
        final byte[] cipheredTextBytes = new byte[bb.remaining()];
        bb.get(cipheredTextBytes);
        final byte[] decryptedTextBytes;

        try {
            decryptedTextBytes = decrypt(cipheredTextBytes, key, new IvParameterSpec(iv));
            return new String(decryptedTextBytes, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.warn("Unable to decrypt text", e);
            throw new ServiceException(e);
        }
    }

    public boolean areFileContentsEncrypted(@Nonnull final File file) throws ServiceException {
        final String fileContents = SERVICE_FACTORY.getFileService().readTextFromFile(file);
        return areFileContentsEncrypted(fileContents);
    }

    private boolean areFileContentsEncrypted(@Nonnull final String text) {
        return text.startsWith(ENCRYPTION_TAG);
    }

    private byte[] encrypt(@Nonnull final String plainText,
                          @Nonnull final SecretKey key,
                          final byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH, iv));
        return cipher.doFinal(plainText.getBytes());
    }

    private byte[] decrypt(@Nonnull final byte[] cipheredText,
                          @Nonnull final SecretKey key,
                          @Nonnull final IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH, iv.getIV()));
        return cipher.doFinal(cipheredText);
    }

    private byte[] generateIv() {
        final byte[] iv = new byte[IV_BYTE_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private String removeEncryptionTagIfPresent(final String cipheredText) {
        return cipheredText.substring(ENCRYPTION_TAG.length());
    }
}
