package nl.danman.file_encryptor.repository.impl;

import edu.umd.cs.findbugs.annotations.NonNull;
import nl.danman.file_encryptor.repository.Repository;
import nl.danman.file_encryptor.repository.RepositoryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileRepositoryImpl implements Repository<File> {

    private static final Logger LOGGER = LogManager.getLogger(FileRepositoryImpl.class);

    @NonNull
    @Override
    public File create(@NonNull final File fileToCreate) throws RepositoryException {
        if (fileToCreate.exists()) {
            throw new RepositoryException(String.format("Creating file failed, file=%s already exists",
                    fileToCreate.getAbsolutePath()));
        }
        try {
            fileToCreate.createNewFile();
        } catch (final Exception e) {
            final String message = String.format("Could not create file=%s", fileToCreate.getAbsolutePath());
            LOGGER.warn(message, e);
            throw new RepositoryException(message, e);
        }
        return fileToCreate;
    }

    @NonNull
    @Override
    public File read(@NonNull final String fileName) throws RepositoryException {
        final File file = new File(fileName);
        if (!file.exists()) {
            throw new RepositoryException(String.format("Reading file unsuccessful, file=%s, does not exist",
                    file.getAbsolutePath()));
        }
        return file;
    }

    /**
     * NO ACTUAL IMPLEMENTATION PROVIDED.
     * Updating a file cannot be done.
     *
     * @param fileToUpdate the file to be updated
     * @return the file provided as is.
     */
    @NonNull
    @Override
    public File update(@NonNull final File fileToUpdate) {
        return fileToUpdate;
    }

    @Override
    public boolean delete(@NonNull final File fileToDelete) throws RepositoryException {
        if (fileToDelete.isDirectory()) {
            throw new RepositoryException(String.format("Delete failed, file=%s is a non-empty derectory",
                    fileToDelete.getAbsolutePath()));
        }
        try {
            return actuallyDeleteFile(fileToDelete);
        } catch (final IOException | SecurityException e) {
            final String message = String.format("File=%s could not be deleted", fileToDelete.getAbsolutePath());
            LOGGER.warn(message, e);
            throw new RepositoryException(message, e);
        }
    }

    /**
     * Wrapper to delete a file. implemented for testing puposes
     *
     * @param fileToDelete .
     * @return success
     * @throws IOException .
     * @throws SecurityException .
     */
    boolean actuallyDeleteFile(final File fileToDelete) throws IOException, SecurityException {
        return Files.deleteIfExists(fileToDelete.toPath());
    }
}
