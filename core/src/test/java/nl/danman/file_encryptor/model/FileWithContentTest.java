package nl.danman.file_encryptor.model;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileWithContentTest {

    @Test
    void instantiationWithNullFileUri_throwsNullPointerException() {
        // Given

        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new FileWithContent(null, ""));

        // Then
        assertEquals("File URI cannot be null", thrown.getMessage());
    }

    @Test
    void getUri_returnsUriOfFile() {
        // Given
        final String uri = "odnfodngv";
        final File file = new File(uri);

        // When
        final FileWithContent fileWithContent = new FileWithContent(uri, "");

        // Then
        assertEquals(file.getAbsolutePath(), fileWithContent.getUri());
    }

    @Test
    void instantiationWithNullContent_throwsNullPointerException() {
        // Given

        // When
        final NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new FileWithContent("", null));

        // Then
        assertEquals("Content cannot be null", thrown.getMessage());
    }
}
