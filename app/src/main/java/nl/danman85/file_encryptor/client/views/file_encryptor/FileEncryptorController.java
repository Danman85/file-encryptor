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
import nl.danman85.file_encryptor.client.views.dialogs.AlertUtil;
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
            if (change.wasRemoved()) {
                lineContainer.getChildren().remove(change.getValueRemoved().getRoot());
            } else {
                this.lineContainer.getChildren().add(change.getValueAdded().getRoot());
            }
        });
    }

    @Override
    public Parent getRoot() {
        return root;
    }

    public void addNewFileLineToLineContainer() {
        selectTextFile().ifPresent(this::addNewFileLineToListOfFileLines);
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

    private void addNewFileLineToListOfFileLines(@Nonnull final File file) {
            if (isFileNotPresent(file)) {
                initializeAndAddFileLine(file);
            } else {
                AlertUtil.createAndShowInformationAlert("File already present");
            }
    }

    private void initializeAndAddFileLine(final File file){
        try {
            final var fileLineViewPair = VIEW_PAIR_FACTORY.getFileLineViewPair();
            fileLineViewPair.getController().setFile(file);
            fileLineViewPair.getController().getCloseButton().setOnAction(event -> removeFileLine(file, fileLineViewPair.getRoot()));
            this.fileLines.put(file, fileLineViewPair.getController());
        } catch (ClientException e) {
            final String warningMessage = "Unable to add FileLine element for file=" + file.getPath();
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }

    private boolean isFileNotPresent(final File file) {
        return !this.fileLines.containsKey(file);
    }

    private void removeFileLine(final File file, final Parent root) {
        this.fileLines.remove(file);
    }
}
