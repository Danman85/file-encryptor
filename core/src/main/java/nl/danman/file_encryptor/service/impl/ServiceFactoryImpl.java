package nl.danman.file_encryptor.service.impl;

import edu.umd.cs.findbugs.annotations.NonNull;
import nl.danman.file_encryptor.repository.impl.RepositoryFactoryImpl;
import nl.danman.file_encryptor.service.ServiceFactory;

public class ServiceFactoryImpl implements ServiceFactory {

    private static class InstanceHolder {
        private static final ServiceFactory SERVICE_FACTORY = new ServiceFactoryImpl();
    }
    private final AESEncryptionService aesEncryptionService;

    private final FileWithContentService fileWithContentService;

    private ServiceFactoryImpl() {
        this.aesEncryptionService = new AESEncryptionService();
        this.fileWithContentService = new FileWithContentService(RepositoryFactoryImpl.getInstance());
    }

    @NonNull
    public static ServiceFactory getInstance() {
        return InstanceHolder.SERVICE_FACTORY;
    }

    @NonNull
    @Override
    public AESEncryptionService getAesEncryptionService() {
        return this.aesEncryptionService;
    }

    @NonNull
    @Override
    public FileWithContentService getFileWithContentService() {
        return this.fileWithContentService;
    }
}
