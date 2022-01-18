package nl.danman85.file_encryptor.service;

import javax.annotation.Nonnull;
import java.io.File;

public interface FileService {

    @Nonnull
    String read(@Nonnull File fileToRead) throws ServiceException;

    void write(@Nonnull String text,
               @Nonnull File fileToWrite) throws ServiceException;
}
