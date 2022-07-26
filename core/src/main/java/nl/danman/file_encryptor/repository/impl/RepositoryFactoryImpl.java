package nl.danman.file_encryptor.repository.impl;

import nl.danman.file_encryptor.repository.Repository;
import nl.danman.file_encryptor.repository.RepositoryFactory;

import java.io.File;

public class RepositoryFactoryImpl implements RepositoryFactory {

    private static RepositoryFactory INSTANCE = null;

    private final Repository<File> fileRepository;

    private RepositoryFactoryImpl() {
        this.fileRepository = new FileRepositoryImpl();
    }

    public static RepositoryFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepositoryFactoryImpl();
        }
        return INSTANCE;
    }

    @Override
    public Repository<File> getFileRepository() {
        return this.fileRepository;
    }
}
