package nl.danman85.file_encryptor.service;

import javax.annotation.Nonnull;

public class ServiceFactoryImpl implements ServiceFactory {

    private static ServiceFactory INSTANCE = null;

    private final FileService fileService;

    private ServiceFactoryImpl() {
        this.fileService = new FileServiceImpl();
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
    public FileService createFileService() {
        return this.fileService;
    }
}
