package nl.danman.file_encryptor.service.impl;

import nl.danman.file_encryptor.model.FileWithContent;
import nl.danman.file_encryptor.repository.RepositoryException;
import nl.danman.file_encryptor.repository.RepositoryFactory;
import nl.danman.file_encryptor.repository.impl.FileRepositoryImpl;
import nl.danman.file_encryptor.service.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileWithContentServiceTest {

    public static final String EXCEPTION_MESSAGE = "oei";

    @Mock
    private RepositoryFactory repositoryFactoryMock;

    @Mock
    private FileRepositoryImpl fileRepositoryMock;

    @Spy
    @InjectMocks
    private FileWithContentService fileWithContentServiceSpy;

    private final FileWithContent fileWithContent = new FileWithContent(
            "lala.txt",
            "Hello, this is some context");

    @BeforeEach
    void setupTest() throws IOException {
        setupMocksAndSpy();
    }

    @Test
    void instantiationWithNullRepositoryFactory_yieldsNullPointerException() {
        // Given

        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new FileWithContentService(null));

        // Then
        assertEquals("RepositoryFactory cannot be null", thrown.getMessage());
    }

    @Test
    void create_persistsNewFileWithContent() throws RepositoryException, ServiceException, IOException {
        // Given

        // When
        this.fileWithContentServiceSpy.create(this.fileWithContent);

        // Then
        verify(fileRepositoryMock).create(eq(this.fileWithContent.file));
        verify(this.fileWithContentServiceSpy).writeTextToFile(eq(this.fileWithContent));
    }

    @Test
    void create_throwsNullPointerWithMessage_whenArgumentIsNull() {
        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> this.fileWithContentServiceSpy.create(null));

        // Then
        assertEquals("FileWithContent cannot be null", thrown.getMessage());
    }

    @Test
    void create_ThrowsServiceException_whenRepositoryException() throws RepositoryException {
        // Given
        doThrow(new RepositoryException(EXCEPTION_MESSAGE)).when(this.fileRepositoryMock).create(any(File.class));

        // When
        final ServiceException thrown = assertThrows(ServiceException.class,
                () -> this.fileWithContentServiceSpy.create(this.fileWithContent));

        // Then
        assertEquals(EXCEPTION_MESSAGE, thrown.getMessage());
        assertInstanceOf(RepositoryException.class, thrown.getCause());
    }

    @Test
    void create_ThrowsServiceException_whenFileUtilsThrowsIOException() throws IOException {
        // Given
        doThrow(new IOException(EXCEPTION_MESSAGE)).when(this.fileWithContentServiceSpy).writeTextToFile(this.fileWithContent);

        // When
        final ServiceException thrown = assertThrows(ServiceException.class,
                () -> this.fileWithContentServiceSpy.create(this.fileWithContent));

        // Then
        assertEquals(EXCEPTION_MESSAGE, thrown.getMessage());
        assertInstanceOf(IOException.class, thrown.getCause());
    }

    @Test
    void read_loadsFileWithContent() throws RepositoryException, IOException {
        // Given
        when(this.fileRepositoryMock.read(anyString())).thenReturn(this.fileWithContent.file);
        doReturn(this.fileWithContent.content).when(this.fileWithContentServiceSpy).readFileToString(any(File.class));

        // When
        final Optional<FileWithContent> fileOptional = this.fileWithContentServiceSpy.read(this.fileWithContent.file.getAbsolutePath());

        // Then
        assertNotNull(fileOptional);
        assertTrue(fileOptional.isPresent());
        assertEquals(this.fileWithContent.file.getAbsolutePath(), fileOptional.get().getUri());
        assertEquals(this.fileWithContent.content, fileOptional.get().content);
    }

    @Test
    void read_throwsNullPointerWithMessage_whenArgumentIsNull() {
        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> this.fileWithContentServiceSpy.read(null));

        // Then
        assertEquals("FileUri cannot be null", thrown.getMessage());
    }

    @Test
    void read_returnsEmtpyOptional_whenRepositoryException() throws RepositoryException {
        // Given
        doThrow(new RepositoryException(EXCEPTION_MESSAGE)).when(this.fileRepositoryMock).read(anyString());

        // When
        final Optional<FileWithContent> optional = fileWithContentServiceSpy.read(fileWithContent.getUri());

        // Then
        assertNotNull(optional);
        assertTrue(optional.isEmpty());
    }

    @Test
    void read_returnsEmptyOptional_whenFileUtilsThrowsIOException() throws IOException, RepositoryException {
        // Given
        when(this.fileRepositoryMock.read(anyString())).thenReturn(this.fileWithContent.file);
        doThrow(new IOException(EXCEPTION_MESSAGE)).when(this.fileWithContentServiceSpy).readFileToString(any(File.class));

        // When
        final Optional<FileWithContent> optional = this.fileWithContentServiceSpy.read(this.fileWithContent.file.getAbsolutePath());

        // Then
        assertNotNull(optional);
        assertTrue(optional.isEmpty());
    }

    @Test
    void update_updatesContent_forGivenFileWithContent(@TempDir Path tempDir) throws ServiceException, IOException,
            RepositoryException {
        // Given
        final File persistedFile = tempDir.resolve(fileWithContent.file.toPath()).toFile();
        persistedFile.createNewFile();
        final FileWithContent oldFile = new FileWithContent(persistedFile.getAbsolutePath(), "old");
        final FileWithContent fileToUpdate = new FileWithContent(persistedFile.getAbsolutePath(), "updated");
        when(this.fileRepositoryMock.read(eq(persistedFile.getAbsolutePath()))).thenReturn(persistedFile);
        doCallRealMethod().when(fileWithContentServiceSpy).writeTextToFile(any());

        // When
        final FileWithContent updatedFile = this.fileWithContentServiceSpy.update(fileToUpdate);

        // Then
        assertNotNull(updatedFile);
        assertEquals(oldFile.file.getAbsolutePath(), updatedFile.file.getAbsolutePath());
        assertEquals("updated", updatedFile.content);
    }

    @Test
    void update_throwsNullPointerWithMessage_whenArgumentIsNull() {
        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> this.fileWithContentServiceSpy.update(null));

        // Then
        assertEquals("cannot perform update operation on null object", thrown.getMessage());
    }

    @Test
    void update_ThrowsServiceException_whenFileUtilsThrowsIOException() throws IOException {
        // Given
        doThrow(new IOException(EXCEPTION_MESSAGE)).when(this.fileWithContentServiceSpy).writeTextToFile(this.fileWithContent);

        // When
        final ServiceException thrown = assertThrows(ServiceException.class,
                () -> this.fileWithContentServiceSpy.update(this.fileWithContent));

        // Then
        assertEquals(EXCEPTION_MESSAGE, thrown.getMessage());
        assertInstanceOf(IOException.class, thrown.getCause());
    }

    @Test
    void update_createsNewFileWhenItDoesNotExist(@TempDir Path tempDir) throws RepositoryException, IOException,
            ServiceException {
        // Given
        final File fileToBeCreated = tempDir.resolve("lala.txt").toFile();
        final FileWithContent toBeUpdated = new FileWithContent(fileToBeCreated.getAbsolutePath(), "updated");
        when(this.fileRepositoryMock.read(eq(fileToBeCreated.getAbsolutePath()))).thenReturn(fileToBeCreated);
        doCallRealMethod().when(fileWithContentServiceSpy).writeTextToFile(any());

        // When
        final FileWithContent newlyCreated = fileWithContentServiceSpy.update(toBeUpdated);

        // Then
        assertNotNull(newlyCreated);
        assertEquals(fileToBeCreated.getAbsolutePath(), newlyCreated.file.getAbsolutePath());
        assertEquals("updated", newlyCreated.content);
        verify(fileWithContentServiceSpy).create(eq(toBeUpdated));
        verify(fileWithContentServiceSpy).read(eq(toBeUpdated.getUri()));
    }

    @Test
    void delete_removesFileWithContent() throws ServiceException, IOException {
        // Given
        doNothing().when(this.fileWithContentServiceSpy).deleteFile(eq(this.fileWithContent.file));

        // When
        final boolean deleted = this.fileWithContentServiceSpy.delete(this.fileWithContent);

        // Then
        assertFalse(deleted);
        verify(this.fileWithContentServiceSpy).deleteFile(eq(this.fileWithContent.file));
    }

    @Test
    void delete_throwsNullPointerWithMessage_whenArgumentIsNull() {
        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> this.fileWithContentServiceSpy.delete(null));

        // Then
        assertEquals("cannot perform delete operation on null object", thrown.getMessage());
    }

    @Test
    void delete_throwsServiceException_whenReceivingIOException() throws IOException {
        // Given
        doThrow(new IOException(EXCEPTION_MESSAGE)).when(this.fileWithContentServiceSpy).deleteFile(eq(this.fileWithContent.file));

        // When
        final ServiceException thrown = assertThrows(ServiceException.class,
                () -> this.fileWithContentServiceSpy.delete(this.fileWithContent));

        // Then
        assertEquals(EXCEPTION_MESSAGE, thrown.getMessage());
        assertInstanceOf(IOException.class, thrown.getCause());
    }

    private void setupMocksAndSpy() throws IOException {
        lenient().doNothing().when(this.fileWithContentServiceSpy).writeTextToFile(any(FileWithContent.class));
        lenient().when(this.repositoryFactoryMock.getFileRepository()).thenReturn(this.fileRepositoryMock);
    }
}
