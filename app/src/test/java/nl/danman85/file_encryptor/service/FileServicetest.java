package nl.danman85.file_encryptor.service;

import nl.danman85.file_encryptor.exception.ResourceException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class FileServicetest {

    private static final ServiceFactory SERVICE_FACTORY = ServiceFactoryImpl.getInstance();

    private FileService fileService;

    @BeforeEach
    void setupTest() {
        this.fileService = SERVICE_FACTORY.getFileService();
    }

    @Test
    void readTextFromFile_throwsResourceException_whenFileNonExistent() {
        // Given
        final File nonExistentFile = new File("C:\\thisFileDoesNotExist.txt");

        // When
        final ServiceException thrown = assertThrows(ServiceException.class, () -> this.fileService.readTextFromFile(nonExistentFile));

        // Then
        assertNotNull(thrown);
        assertEquals("Cannot perform operations on non-existing file=" + nonExistentFile.getPath(), thrown.getMessage());
    }

    @Test
    void readTextFromFile_returnsTextInTestFile() throws ServiceException, IOException {
        // Given
        final File testFile = new File(Objects.requireNonNull(getClass().getResource("/testFile.txt")).getPath());

        // When
        final String fileText = this.fileService.readTextFromFile(testFile);

        // Then
        assertNotNull(fileText);
        assertEquals(FileUtils.readFileToString(testFile, StandardCharsets.UTF_8), fileText);
    }

    @Test
    void writeTextToFile_throwsResourceException_whenFileNonExistent() {
        // Given
        final File nonExistentFile = new File("C:\\thisFileDoesNotExist.txt");

        // When
        final ServiceException thrown = assertThrows(ServiceException.class, () -> this.fileService.writeTextToFile("", nonExistentFile));

        // Then
        assertNotNull(thrown);
        assertEquals("Cannot perform operations on non-existing file=" + nonExistentFile.getPath(), thrown.getMessage());
    }

    @Test
    void writeTextToFile_writesTextToFile() throws ServiceException, IOException {
        // Given
        final File emptyTestFile = createNewFile();
        final String textToStore = "This is saved text!\nHah!\n\nAaaaand, it is over";

        // When
        this.fileService.writeTextToFile(textToStore, emptyTestFile);

        // Then
        assertEquals(FileUtils.readFileToString(emptyTestFile, StandardCharsets.UTF_8), textToStore);
    }

    private File createNewFile() throws IOException {
        return new File(Files.createTempFile("temp", ".txt").toUri());
    }
}
