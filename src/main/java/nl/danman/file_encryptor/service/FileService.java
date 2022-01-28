package nl.danman.file_encryptor.service;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.File;

public interface FileService {

    @NonNull
    String read(@NonNull File fileToRead) throws ServiceException;

    void write(@NonNull String text,
               @NonNull File fileToWrite) throws ServiceException;
}
