package nl.danman.file_encryptor.repository.impl;

import nl.danman.file_encryptor.repository.Repository;
import nl.danman.file_encryptor.repository.RepositoryException;
import nl.danman.file_encryptor.repository.RepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileRepositoryImplTest {

    private static final RepositoryFactory REPOSITORY_FACTORY = RepositoryFactoryImpl.getInstance();

    private Repository<File> repository;

    @TempDir
    private File testDir;

    @BeforeEach
    void setupTest() {
        this.repository = REPOSITORY_FACTORY.getFileRepository();
    }

    @Test
    void create_persistsFileCorrectly() throws RepositoryException {
        // Given
        final File testFile = new File(testDir.getAbsolutePath() + "TestFile");
        assertFalse(testFile.exists());

        // When
        final File createdFile = this.repository.create(testFile);

        // Then
        assertNotNull(createdFile);
        assertTrue(createdFile.exists());
    }

    @Test
    void create_throwsRepositoryException_whenFileExists() throws IOException {
        // Given
        final String testFileURI = testDir.getAbsolutePath() + "TestFile";
        final File testFile = new File(testFileURI);
        testFile.createNewFile();
        assertTrue(testFile.exists());

        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> this.repository.create(testFile));

        // Then
        assertEquals(String.format("Creating file failed, file=%s already exists",
                testFile.getAbsolutePath()), thrown.getMessage());
    }

    @Test
    void create_throwsRepositoryException_whenIOExceptionIsThrown() throws IOException {
        // Given
        final File testFile = mock(File.class);
        when(testFile.getAbsolutePath()).thenReturn("lala.txt");
        doThrow(IOException.class).when(testFile).createNewFile();

        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> this.repository.create(testFile));

        // Then
        assertInstanceOf(IOException.class, thrown.getCause());
        assertEquals(String.format("Could not create file=%s", testFile.getAbsolutePath()), thrown.getMessage());
    }

    @Test
    void create_throwsRepositoryException_whenSecurityExceptionIsThrown() throws IOException {
        // Given
        final File testFile = mock(File.class);
        when(testFile.getAbsolutePath()).thenReturn("lala");
        doThrow(SecurityException.class).when(testFile).createNewFile();

        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> this.repository.create(testFile));

        // Then
        assertInstanceOf(SecurityException.class, thrown.getCause());
        assertEquals(String.format("Could not create file=%s", testFile.getAbsolutePath()), thrown.getMessage());
    }

    @Test
    void update_returnsTheInputFile() throws RepositoryException {
        // Given
        final File testFile = new File("lala.txt");

        // When
        final File updatedFile = this.repository.update(testFile);

        // Then
        assertNotNull(updatedFile);
        assertEquals(testFile.hashCode(), updatedFile.hashCode());
    }

    @Test
    void read_succeeds() throws IOException, RepositoryException {
        // Given
        final File testFile = new File(this.testDir, "lala.txt");
        testFile.createNewFile();

        // When
        final File readFile = this.repository.read(testFile.getAbsolutePath());

        // Then
        assertNotNull(readFile);
        assertEquals(testFile.hashCode(), readFile.hashCode());
    }

    @Test
    void read_throwsRepositoryException_whenFileDoesNotExist() {
        // Given
        final File nonPersistedFile = new File("lala.txt");

        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> this.repository.read(nonPersistedFile.getAbsolutePath()));

        // Then
        assertEquals(String.format("Reading file unsuccessful, file=%s, does not exist",
                nonPersistedFile.getAbsolutePath()), thrown.getMessage());
    }

    @Test
    void delete_succeeds() throws IOException, RepositoryException {
        // Given
        final File existingFile = new File(this.testDir, "lala.txt");
        existingFile.createNewFile();

        // When
        final boolean isDeleted = this.repository.delete(existingFile);

        // Then
        assertTrue(isDeleted);
        assertFalse(existingFile.exists());
    }


    @Test
    void delete_throwsRepositoryException_whenFileIsDirectory() {
        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> this.repository.delete(this.testDir));

        // Then
        assertEquals(String.format("Delete failed, file=%s is a non-empty derectory", this.testDir.getAbsolutePath()),
                thrown.getMessage());
    }

    @Test
    void delete_rethrowsIOException_asRepositoryException() throws IOException {
        // Given
        final FileRepositoryImpl repoSpy = (FileRepositoryImpl) spy(this.repository);
        doThrow(IOException.class).when(repoSpy).actuallyDeleteFile(any(File.class));
        final File file = new File(this.testDir, "lala.txt");
        file.createNewFile();

        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> repoSpy.delete(file));

        // Then
        assertEquals(String.format("File=%s could not be deleted", file.getAbsolutePath()), thrown.getMessage());
        assertInstanceOf(IOException.class, thrown.getCause());
    }

    @Test
    void delete_rethrowsSecurityException_asRepositoryException() throws IOException {
        // Given
        final FileRepositoryImpl repoSpy = (FileRepositoryImpl) spy(this.repository);
        doThrow(SecurityException.class).when(repoSpy).actuallyDeleteFile(any(File.class));
        final File file = new File(this.testDir, "lala.txt");
        file.createNewFile();

        // When
        final RepositoryException thrown = assertThrows(RepositoryException.class,
                () -> repoSpy.delete(file));

        // Then
        assertEquals(String.format("File=%s could not be deleted", file.getAbsolutePath()), thrown.getMessage());
        assertInstanceOf(SecurityException.class, thrown.getCause());
    }
}
