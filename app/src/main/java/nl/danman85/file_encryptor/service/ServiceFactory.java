package nl.danman85.file_encryptor.service;

import javax.annotation.Nonnull;

public interface ServiceFactory {

    @Nonnull
    FileServiceImpl getFileService();

    @Nonnull
    AESEncryptionService getAesEncryptionService();
}
