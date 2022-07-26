package nl.danman.file_encryptor.repository;

import java.io.File;

public interface RepositoryFactory {

    Repository<File> getFileRepository();
}
