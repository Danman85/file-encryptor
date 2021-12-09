package nl.danman85.file_encryptor.repository;

import nl.danman85.file_encryptor.exception.ResourceException;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileRepository implements Repository {

    private static final String CORRECT_FILE_NAME_REGEX = "(/[A-Za-z])\\w+";
    private Path filePath;

    protected FileRepository(final @Nonnull String fileName) throws ResourceException {
        this.filePath = getPathFromFileName(fileName);
    }

    @Override
    @Nonnull
    public List<String> getTextAsListOfLines() throws ResourceException {
        try {
            return extractTextFromFile();
        } catch (IOException e) {
            throw new ResourceException("Could not read from file: " + filePath.getFileName(), e);
        }
    }

    @Override
    public void writeText(final @Nonnull String text) throws ResourceException {
        try {
            writeTextToFile(text);
        } catch (IOException e) {
            throw new ResourceException("Could not write to file: " + filePath.getFileName(), e);
        }
    }

    protected List<String> extractTextFromFile() throws IOException {
        return FileUtils.readLines(filePath.toFile(), StandardCharsets.UTF_8);
    }

    private Path getPathFromFileName(final String fileName) throws ResourceException {
        final URL url = getURLFromFileNameOrThrowException(fileName);
        return getPathOrThrowException(url);
    }

    private @Nonnull URL getURLFromFileNameOrThrowException(final @Nonnull String fileName) throws ResourceException {
        final URL fileUrl = FileRepository.class.getResource(fileName);
        if (fileUrl == null) {
            throwResourceException(fileName, null);
        }
        return fileUrl;
    }

    private Path getPathOrThrowException(final @Nonnull URL url) throws ResourceException {
        Path filePath = null;
        try {
            filePath = Paths.get(url.toURI());
        } catch (final Exception e) {
            throwResourceException(url.getFile(), e);
        }
        return filePath;
    }

    private void throwResourceException(final @Nonnull String fileName, final @Nullable Throwable thrown) throws ResourceException {
        final String message = "File URL not found for file: ";
        if (thrown == null) {
            throw new ResourceException(message + fileName);
        } else {
            throw new ResourceException(message + fileName, thrown);
        }

    }

    protected void writeTextToFile(final String text) throws IOException {
        FileUtils.write(filePath.toFile(), text, StandardCharsets.UTF_8);
    }
}
