package nl.danman85.file_encryptor.repository;

import nl.danman85.file_encryptor.exception.ResourceException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.MissingResourceException;

public interface Repository {

    @Nonnull
    List<String> getTextAsListOfLines() throws MissingResourceException, IOException, ResourceException;

    void writeText(final @Nonnull String text) throws MissingResourceException, IOException, ResourceException;
}
