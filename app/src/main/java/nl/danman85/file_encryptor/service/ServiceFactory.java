package nl.danman85.file_encryptor.service;

import javax.annotation.Nonnull;

public interface ServiceFactory {

    @Nonnull
    FileService getFileService();

    @Nonnull
    AESEncryptionService getAesEncryptionService();
}
