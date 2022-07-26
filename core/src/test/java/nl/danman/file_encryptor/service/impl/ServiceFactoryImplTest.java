package nl.danman.file_encryptor.service.impl;

import nl.danman.file_encryptor.service.ServiceFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceFactoryImplTest {

    private final ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();

    @Test
    void getInstance_returnsNonnullObject() {
        // When
        final ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();

        // Then
        assertNotNull(serviceFactory);
    }

    @Test
    void getInstance_repeatedlyReturnsTheSameObject() {
        // When
        final ServiceFactory serviceFactory1 = ServiceFactoryImpl.getInstance();
        final ServiceFactory serviceFactory2 = ServiceFactoryImpl.getInstance();

        // Then
        assertSame(serviceFactory1, serviceFactory2);
        assertEquals(serviceFactory1.hashCode(), serviceFactory2.hashCode());
    }

    @Test
    void createAESEncryptionService_returnsAESEncryptionServiceObject() {
        // When
        final AESEncryptionService aesEncryptionService = this.serviceFactory.getAesEncryptionService();

        // Then
        assertNotNull(aesEncryptionService);
    }

    @Test
    void createFileService_returnsFileServiceObject() {
        // When
        final FileWithContentService fileService = this.serviceFactory.getFileWithContentService();

        // Then
        assertNotNull(fileService);
    }
}
