package nl.danman85.file_encryptor.client.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Pair;
import nl.danman85.file_encryptor.client.ClientException;
import nl.danman85.file_encryptor.client.configuration.FXMLViewSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

public class ViewPairFactory {

    private static ViewPairFactory instance;

    private static final Logger LOGGER = LogManager.getLogger(ViewPairFactory.class);

    private ViewPairFactory() {
    }

    public static ViewPairFactory getInstance() {
        if (instance == null) {
            instance = new ViewPairFactory();
        }
        return instance;
    }

    /**
     *  Returns a Pair, consisting of the view controller, and root object for a given FXML source.
     * @param fxmlViewSource the fxml uri for a view
     * @return {@link Pair}<{@link Parent}, C>, where C must be a Controller implementation
     * @throws ClientException exception
     */
    @Nonnull
    public <C extends Controller> Pair<Parent, C> getViewPairForViewResource(@Nonnull final FXMLViewSource fxmlViewSource) throws ClientException {
        final var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(fxmlViewSource.getResourceUrl())));
        C controller;
        Parent root;
        try {
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            LOGGER.warn("Unable to load view=" + fxmlViewSource.name(), e);
            throw new ClientException(e);
        }
        return new Pair<>(root, controller);
    }
}
