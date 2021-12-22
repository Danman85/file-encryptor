package nl.danman85.file_encryptor.service;

import javax.annotation.Nonnull;

public class ServiceFactoryImpl implements ServiceFactory {

    private static ServiceFactory INSTANCE = null;

    private final FileService fileService;
    private final AESEncryptionService aesEncryptionService;

    private ServiceFactoryImpl() {
        this.fileService = new FileService();
        this.aesEncryptionService = new AESEncryptionService();
    }

    @Nonnull
    public static ServiceFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactoryImpl();
        }
        return INSTANCE;
    }

    @Nonnull
    @Override
    public FileService getFileService() {
        return this.fileService;
    }

    @Nonnull
    @Override
    public AESEncryptionService getAesEncryptionService() {
        return this.aesEncryptionService;
    }
}
