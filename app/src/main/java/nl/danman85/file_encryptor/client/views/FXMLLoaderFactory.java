package nl.danman85.file_encryptor.client.views;

import javafx.fxml.FXMLLoader;
import nl.danman85.file_encryptor.client.configuration.FXMLViewSource;

import javax.annotation.Nonnull;

public class FXMLLoaderFactory {

    public FXMLLoaderFactory() {
    }

    @Nonnull
    public FXMLLoader getFXMLLoaderForResource(@Nonnull final FXMLViewSource fxmlViewSource) {
        return new FXMLLoader(getClass().getResource(fxmlViewSource.getResourceUrl()));
    }
}
