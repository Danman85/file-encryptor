package nl.danman85.file_encryptor.client.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Pair;
import nl.danman85.file_encryptor.client.ClientException;
import nl.danman85.file_encryptor.client.configuration.FXMLViewSource;
import nl.danman85.file_encryptor.client.views.file_encryptor.FileEncryptorController;
import nl.danman85.file_encryptor.client.views.file_encryptor.FileLineController;
import nl.danman85.file_encryptor.client.views.main.MainViewController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

public class FXMLViewPairFactory {

    private static final Logger LOGGER = LogManager.getLogger(FXMLViewPairFactory.class);

    private static FXMLViewPairFactory instance;

    private FXMLViewPairFactory() {
    }

    public static FXMLViewPairFactory getInstance() {
        if (instance == null) {
            instance = new FXMLViewPairFactory();
        }
        return instance;
    }

    public FXMLViewPair<MainViewController> getMainViewPair() throws ClientException {
        final var mainViewPair = getViewPairForViewResource(FXMLViewSource.MAIN_VIEW);
        if (mainViewPair.getController() instanceof MainViewController) {
            return new FXMLViewPair<>(mainViewPair.getRoot(), (MainViewController) mainViewPair.getController());
        } else {
            final String warningMessage = "Could not create ViewPair for File Line, Controller is not of expected type=" + MainViewController.class;
            throw logWarningAndReturnClientExceptionForMessage(warningMessage);
        }
    }

    public FXMLViewPair<FileEncryptorController> getFileEncryptorViewPair() throws ClientException {
        final var mainViewPair = getViewPairForViewResource(FXMLViewSource.FILE_ENCRYPTOR);
        if (mainViewPair.getController() instanceof FileEncryptorController) {
            return new FXMLViewPair<>(mainViewPair.getRoot(), (FileEncryptorController) mainViewPair.getController());
        } else {
            final String warningMessage = "Could not create ViewPair for File Line, Controller is not of expected type=" + FileEncryptorController.class;
            throw logWarningAndReturnClientExceptionForMessage(warningMessage);
        }
    }

    public FXMLViewPair<FileLineController> getFileLineViewPair() throws ClientException {
        final var mainViewPair = getViewPairForViewResource(FXMLViewSource.FILE_LINE);
        if (mainViewPair.getController() instanceof FileLineController) {
            return new FXMLViewPair<>(mainViewPair.getRoot(), (FileLineController) mainViewPair.getController());
        } else {
            final String warningMessage = "Could not create ViewPair for File Line, Controller is not of expected type=" + FileLineController.class;
            throw logWarningAndReturnClientExceptionForMessage(warningMessage);
        }
    }

    private ClientException logWarningAndReturnClientExceptionForMessage(final String warningMessage) {
        LOGGER.warn(warningMessage);
        return new ClientException(warningMessage);
    }

    /**
     *  Returns a Pair, consisting of the view controller, and root object for a given FXML source.
     * @param fxmlViewSource the fxml uri for a view
     * @return {@link Pair}<{@link Parent}, C>, where C must be a Controller implementation
     * @throws ClientException exception
     */
    @Nonnull
    private <C extends Controller> FXMLViewPair<C> getViewPairForViewResource(@Nonnull final FXMLViewSource fxmlViewSource) throws ClientException {
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
        return new FXMLViewPair<>(root, controller);
    }
}
