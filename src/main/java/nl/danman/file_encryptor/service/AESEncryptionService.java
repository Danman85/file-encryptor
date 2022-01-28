package nl.danman.file_encryptor.service;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    @NonNull
    public SecretKey getKeyFromPassword(@NonNull final String password,
                                        @NonNull final String salt)
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

    @NonNull
    public String encryptWithPrefixIv(@NonNull final String plainText,
                                      @NonNull final SecretKey key) throws ServiceException {
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

    @NonNull
    public String decryptWithPrefixIv(@NonNull final String cipheredText,
                                      @NonNull final SecretKey key) throws ServiceException {
        final String unTaggedCipheredText = removeEncryptionTagIfPresent(cipheredText);

        final ByteBuffer bb = ByteBuffer.wrap(Base64.getDecoder().decode(unTaggedCipheredText));
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

    public boolean areFileContentsEncrypted(@NonNull final File file) throws ServiceException {
        final String fileContents = SERVICE_FACTORY.getFileService().read(file);
        return areFileContentsEncrypted(fileContents);
    }

    private boolean areFileContentsEncrypted(@NonNull final String text) {
        return text.startsWith(ENCRYPTION_TAG);
    }

    private byte[] encrypt(@NonNull final String plainText,
                          @NonNull final SecretKey key,
                          final byte[] iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH, iv));
        return cipher.doFinal(plainText.getBytes());
    }

    private byte[] decrypt(@NonNull final byte[] cipheredText,
                          @NonNull final SecretKey key,
                          @NonNull final IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
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

    private String removeEncryptionTagIfPresent(final String cipheredText) throws ServiceException {
        if (areFileContentsEncrypted(cipheredText)) {
            return cipheredText.substring(ENCRYPTION_TAG.length());
        } else {
            throw new ServiceException("Unable to decrypt an unencrypted text");
        }
    }
}
