package nl.danman85.file_encryptor.client.views.file_encryptor;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nl.danman85.file_encryptor.client.ClientException;
import nl.danman85.file_encryptor.client.views.Controller;
import nl.danman85.file_encryptor.client.views.ViewPairFactory;
import nl.danman85.file_encryptor.client.views.alerts.AlertUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Optional;

public class FileEncryptorController implements Controller {

    private static final Logger LOGGER = LogManager.getLogger(FileEncryptorController.class);

    private static final ViewPairFactory VIEW_PAIR_FACTORY = ViewPairFactory.getInstance();

    @FXML private VBox root;
    @FXML private Button newFileButton;
    @FXML private VBox lineContainer;
    private final ObservableMap<File, FileLineController> fileLines = FXCollections.synchronizedObservableMap(
            FXCollections.observableHashMap());

    public FileEncryptorController() {
    }

    public void initialize() {
        initListeners();
    }

    private void initListeners() {
        this.fileLines.addListener((MapChangeListener<File, FileLineController>) change -> {
            final Parent fileLineRoot = change.getValueAdded().getRoot();
            if (change.wasRemoved()) {
                lineContainer.getChildren().remove(fileLineRoot);
            } else {
                this.lineContainer.getChildren().add(fileLineRoot);
            }
        });
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public void addNewFileLineToLineContainer() {
        selectTextFile().ifPresent(this::addNewUniqueLineElementToListOfFileLines);
    }

    private Optional<File> selectTextFile() {
        final var fileChooser = new FileChooser();
        final var currentWindow = this.newFileButton.getScene().getWindow();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        final File selectedFile = fileChooser.showOpenDialog(currentWindow);

        if (selectedFile == null) {
            LOGGER.info("Unsuccesful in opening a file");
            return Optional.empty();
        } else {
            LOGGER.info("Selected file=" + selectedFile.getPath());
            return Optional.of(selectedFile);
        }
    }

    private void addNewUniqueLineElementToListOfFileLines(@Nonnull final File file) {
        try {
            final var fileLineViewPair = VIEW_PAIR_FACTORY.getFileLineViewPair();
            if (fileLinesDoesNotHaveLineForFile(file)) {
                this.fileLines.put(file, fileLineViewPair.getValue());
            } else {
                AlertUtil.createAndShowInformationAlert("File already present");
            }
        } catch (ClientException e) {
            final String warningMessage = "Unable to add FileLine element for file=" + file.getPath();
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }

    private boolean fileLinesDoesNotHaveLineForFile(final File file) {
//        boolean fileAlreadyPresent = false;
//        synchronized (this.fileLines) {
//            if (!this.fileLines.containsKey(file)) {
//                fileAlreadyPresent = true;
//            }
//        }
//        return fileAlreadyPresent;
        return !this.fileLines.containsKey(file);
    }
}
