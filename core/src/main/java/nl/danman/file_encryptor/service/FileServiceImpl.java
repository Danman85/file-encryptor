package nl.danman.file_encryptor.service;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LogManager.getLogger(FileServiceImpl.class);

    @Override
    @NonNull
    public String read(@NonNull final File fileToRead) throws ServiceException {
        validateFileExistence(fileToRead);
        String fileText = "";
        try {
            fileText = FileUtils.readFileToString(fileToRead, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logWarningAndThrowException(fileToRead, e, "Unable to read from file=");
        }
        return fileText;
    }

    @Override
    public void write(@NonNull final String text,
                      @NonNull final File fileToWrite) throws ServiceException {
        validateFileExistence(fileToWrite);
        try {
            FileUtils.write(fileToWrite, text, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logWarningAndThrowException(fileToWrite, e, "Unable to store text to file=");
        }
    }

    private void logWarningAndThrowException(@NonNull final File file, final IOException e, final String s) throws ServiceException {
        LOGGER.warn(s + file.getPath(), e);
        throw new ServiceException(e);
    }

    private void validateFileExistence(@NonNull final File file) throws ServiceException {
        if (!file.exists()) {
            final String message = "Cannot perform operations on non-existing file=" + file.getPath();
            LOGGER.warn(message);
            throw new ServiceException(message);
        }
    }

}
