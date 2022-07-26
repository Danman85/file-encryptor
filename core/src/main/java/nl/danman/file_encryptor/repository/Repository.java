package nl.danman.file_encryptor.repository;

import edu.umd.cs.findbugs.annotations.NonNull;

public interface Repository<T> {

    @NonNull
    T create(@NonNull final T objectToCreate) throws RepositoryException;

    @NonNull
    T read(@NonNull final String id) throws RepositoryException;

    @NonNull
    T update(@NonNull final T objectToUpdate) throws RepositoryException;

    boolean delete(@NonNull final T objectToDelete) throws RepositoryException;
}
