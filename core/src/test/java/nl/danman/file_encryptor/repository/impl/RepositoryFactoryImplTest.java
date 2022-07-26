package nl.danman.file_encryptor.repository.impl;

import nl.danman.file_encryptor.repository.Repository;
import nl.danman.file_encryptor.repository.RepositoryFactory;
import nl.danman.file_encryptor.repository.impl.FileRepositoryImpl;
import nl.danman.file_encryptor.repository.impl.RepositoryFactoryImpl;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryFactoryImplTest {

    private final RepositoryFactory repositoryFactory = RepositoryFactoryImpl.getInstance();

    @Test
    void getInstance_returnsNonnullFactory() {
        // When
        final RepositoryFactory repositoryFactory = RepositoryFactoryImpl.getInstance();

        // Then
        assertNotNull(repositoryFactory);
    }

    @Test
    void repeatedGetInstance_returnsTheSameObjectEveryTime() {
        // When
        final RepositoryFactory repositoryFactory1 = RepositoryFactoryImpl.getInstance();
        final RepositoryFactory repositoryFactory2 = RepositoryFactoryImpl.getInstance();

        // Then
        assertSame(repositoryFactory1, repositoryFactory2);
        assertEquals(repositoryFactory1.hashCode(), repositoryFactory2.hashCode());
    }

    @Test
    void getFileRepository_returnsNonnullFileRepository() {
        // When
        final Repository<File> fileRepository = repositoryFactory.getFileRepository();

        // Then
        assertNotNull(fileRepository);
        assertInstanceOf(FileRepositoryImpl.class, fileRepository);
    }
}
