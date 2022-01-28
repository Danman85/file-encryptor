package nl.danman.file_encryptor.service;

import edu.umd.cs.findbugs.annotations.NonNull;

public class ServiceFactoryImpl implements ServiceFactory {

    private static ServiceFactory INSTANCE = null;

    private final FileServiceImpl fileService;
    private final AESEncryptionService aesEncryptionService;

    private ServiceFactoryImpl() {
        this.fileService = new FileServiceImpl();
        this.aesEncryptionService = new AESEncryptionService();
    }

    @NonNull
    public static ServiceFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceFactoryImpl();
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public FileServiceImpl getFileService() {
        return this.fileService;
    }

    @NonNull
    @Override
    public AESEncryptionService getAesEncryptionService() {
        return this.aesEncryptionService;
    }
}
