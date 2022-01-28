package nl.danman.file_encryptor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ServiceFactoryImplTest {

    private final ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();

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
    void createFileService_returnsFileServiceObject() {
        // When
        final FileServiceImpl fileService = this.serviceFactory.getFileService();

        // Then
        assertNotNull(fileService);
    }
}
