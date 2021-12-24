package nl.danman85.file_encryptor.service;

import nl.danman85.file_encryptor.service.encryption.AESEncryptionService;

import javax.annotation.Nonnull;

public interface ServiceFactory {

    @Nonnull
    FileService getFileService();

    @Nonnull
    AESEncryptionService getAesEncryptionService();
}
