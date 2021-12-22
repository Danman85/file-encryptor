package nl.danman85.file_encryptor.service;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileService {

    private static final Logger LOGGER = LogManager.getLogger(FileService.class);

    @Nonnull
    public String readTextFromFile(@Nonnull final File fileToRead) throws ServiceException {
        validateFileExistence(fileToRead);
        String fileText = "";
        try {
            fileText = FileUtils.readFileToString(fileToRead, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logWarningAndThrowException(fileToRead, e, "Unable to read from file=");
        }
        return fileText;
    }

    public void writeTextToFile(@Nonnull final String text,
                                @Nonnull final File fileToWrite) throws ServiceException {
        validateFileExistence(fileToWrite);
        try {
            FileUtils.write(fileToWrite, text, Charsets.UTF_8);
        } catch (IOException e) {
            logWarningAndThrowException(fileToWrite, e, "Unable to store text to file=");
        }
    }

    private void logWarningAndThrowException(@Nonnull final File file, final IOException e, final String s) throws ServiceException {
        LOGGER.warn(s + file.getPath(), e);
        throw new ServiceException(e);
    }

    private void validateFileExistence(@Nonnull final File file) throws ServiceException {
        if (!file.exists()) {
            final String message = "Cannot perform operations on non-existing file=" + file.getPath();
            LOGGER.warn(message);
            throw new ServiceException(message);
        }
    }

}
