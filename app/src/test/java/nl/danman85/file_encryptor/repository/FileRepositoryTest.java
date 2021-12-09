package nl.danman85.file_encryptor.repository;

import nl.danman85.file_encryptor.exception.ResourceException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

public class FileRepositoryTest {

    public static final String TEST_FILE = "/testFile.txt";

    @Test
    void instantiation_throwsMissingResourceException_whenFileDoesNotExist() {
        // Given
        final String nonExistingFile = "non-existing-file.txt";

        // When
        final ResourceException thrown = assertThrows(ResourceException.class,
                () -> new FileRepository(nonExistingFile));

        // Then
        assertNotNull(thrown);
        assertThat(thrown.getMessage(), is("File URL not found for file: " + nonExistingFile));
    }

    @Test
    void getText_returnsContentsOfFileAsListOfString() throws Exception {
        // Given
        final String fileName = TEST_FILE;
        final FileRepository fileHandler = new FileRepository(fileName);
        final List<String> testLines = readLines(fileName);

        // When
        final List<String> stringList = fileHandler.getTextAsListOfLines();

        // Then
        assertNotNull(stringList);
        assertEquals(testLines, stringList);
    }

    @Test
    void getTextRethrowsIOException_asResourceException() throws ResourceException, IOException {
        // Given
        final FileRepository fileHandlerSpy = spy(new FileRepository(TEST_FILE));
        doThrow(IOException.class).when(fileHandlerSpy).extractTextFromFile();

        // When
        final ResourceException thrown = assertThrows(ResourceException.class,
                () -> fileHandlerSpy.getTextAsListOfLines());

        // Then
        assertNotNull(thrown);
        assertThat(thrown.getCause(), instanceOf(IOException.class));
        assertThat(thrown.getMessage(), is("Could not read from file: testFile.txt"));
    }

    @Test
    void writeText_writesLinesCorrectly() throws Exception {
        // Given
        final String newText = "This is...\nWell, \nA new text!";
        final FileRepository fileHandler = new FileRepository(TEST_FILE);

        // When
        fileHandler.writeText(newText);

        // Then
        assertThat(readLines(TEST_FILE).toArray(), is(newText.split("\n")));
    }

    @Test
    void writeText_rethrowsIOExceptionAsIOException() throws Exception {
        // Given
        final FileRepository fileHandler = spy(new FileRepository(TEST_FILE));
        doThrow(IOException.class).when(fileHandler).writeTextToFile(anyString());

        // When
        final ResourceException thrown = assertThrows(ResourceException.class,
                () -> fileHandler.writeText("He clown!"));

        // Then
        assertNotNull(thrown);
        assertThat(thrown.getCause(), instanceOf(IOException.class));
        assertThat(thrown.getMessage(), is("Could not write to file: testFile.txt"));
    }

    private List<String> readLines(final String fileName) throws IOException {
        final File file = new File(Objects.requireNonNull(FileRepositoryTest.class.getResource(fileName)).getFile());
        return FileUtils.readLines(file, StandardCharsets.UTF_8);
    }


}
