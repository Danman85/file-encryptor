package nl.danman.file_encryptor.service.impl;

import nl.danman.file_encryptor.model.FileWithContent;
import nl.danman.file_encryptor.repository.RepositoryException;
import nl.danman.file_encryptor.repository.RepositoryFactory;
import nl.danman.file_encryptor.service.ServiceException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class FileWithContentService {

    private final RepositoryFactory repositoryFactory;

    public FileWithContentService(final RepositoryFactory repositoryFactory) {
        this.repositoryFactory = Objects.requireNonNull(repositoryFactory, "RepositoryFactory cannot be null");
    }

    public FileWithContent create(final FileWithContent fileWithContent) throws ServiceException {
        Objects.requireNonNull(fileWithContent, "FileWithContent cannot be null");
        try {
            this.repositoryFactory.getFileRepository().create(fileWithContent.file);
            writeTextToFile(fileWithContent);
            return fileWithContent;
        } catch (RepositoryException | IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public Optional<FileWithContent> read(final String fileUri) {
        Objects.requireNonNull(fileUri, "FileUri cannot be null");
        try {
            final File file = this.repositoryFactory.getFileRepository().read(fileUri);
            return Optional.of(new FileWithContent(file.getAbsolutePath(), readFileToString(file)));
        } catch (RepositoryException | IOException e) {
            return Optional.empty();
        }
    }

    public FileWithContent update(final FileWithContent fileWithContentToUpdate) throws ServiceException {
        Objects.requireNonNull(fileWithContentToUpdate, "cannot perform update operation on null object");
        try {
            final FileWithContent fileWithContent = read(fileWithContentToUpdate.getUri())
                    .orElse(create(fileWithContentToUpdate));
            writeTextToFile(fileWithContent);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return read(fileWithContentToUpdate.getUri()).orElseThrow();
    }

    public boolean delete(final FileWithContent fileWithContent) throws ServiceException {
        Objects.requireNonNull(fileWithContent, "cannot perform delete operation on null object");
        try {
            deleteFile(fileWithContent.file);
        } catch (final IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return fileWithContent.file.exists();
    }

    void writeTextToFile(final FileWithContent fileWithContent) throws IOException {
        FileUtils.write(fileWithContent.file, fileWithContent.content, StandardCharsets.UTF_8);
    }

    String readFileToString(final File file) throws IOException {
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    void deleteFile(final File file) throws IOException {
        FileUtils.delete(file);
    }
}
