package nl.danman.file_encryptor.client.views.file_encryptor;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import nl.danman.file_encryptor.client.ClientException;
import nl.danman.file_encryptor.client.model.EncryptionFile;
import nl.danman.file_encryptor.client.views.Controller;
import nl.danman.file_encryptor.client.views.FXMLViewPairFactory;
import nl.danman.file_encryptor.client.views.dialogs.AlertUtil;
import nl.danman.file_encryptor.model.FileWithContent;
import nl.danman.file_encryptor.service.ServiceFactory;
import nl.danman.file_encryptor.service.impl.ServiceFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Optional;

public class FileEncryptorController implements Controller {

    private static final Logger LOGGER = LogManager.getLogger(FileEncryptorController.class);

    private static final FXMLViewPairFactory VIEW_PAIR_FACTORY = FXMLViewPairFactory.getInstance();

    private static final ServiceFactory SERVICE_FACTORY = ServiceFactoryImpl.getInstance();

    @FXML private VBox root;
    @FXML private Button newFileButton;
    @FXML private VBox lineContainer;
    private final ObservableMap<EncryptionFile, FileLineController> fileLines = FXCollections.synchronizedObservableMap(
            FXCollections.observableHashMap());

    public FileEncryptorController() {
    }

    public void initialize() {
        initListeners();
    }

    private void initListeners() {
        this.fileLines.addListener((MapChangeListener<EncryptionFile, FileLineController>) change -> {
            if (change.wasRemoved()) {
                lineContainer.getChildren().remove(change.getValueRemoved().getRoot());
            } else {
                this.lineContainer.getChildren().add(change.getValueAdded().getRoot());
            }
        });
    }

    @NonNull
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

    private void addNewFileLineToListOfFileLines(@NonNull final File file) {
        final Optional<FileWithContent> fileWithContent = SERVICE_FACTORY.getFileWithContentService()
                .read(file.getAbsolutePath());
        if (fileWithContent.isEmpty()) {
            AlertUtil.createAndShowInformationAlert("File=" + file.getAbsolutePath() +" not present");
        } else {
            final EncryptionFile encryptionFile = new EncryptionFile(fileWithContent.get());
            if (isEncryptionFileNotPresentInLoadedFiles(encryptionFile)) {
                initializeAndAddFileLine(encryptionFile);
            } else {
                AlertUtil.createAndShowInformationAlert("File already present");
            }
        }
        final EncryptionFile encryptionFile;
    }

    private void initializeAndAddFileLine(@NonNull final EncryptionFile encryptionFile){
        try {
            final var fileLineViewPair = VIEW_PAIR_FACTORY.getFileLineViewPair();
            fileLineViewPair.getController().init(encryptionFile);
            fileLineViewPair.getController().getCloseButton().setOnAction(event -> removeFileLine(encryptionFile, fileLineViewPair.getRoot()));
            this.fileLines.put(encryptionFile, fileLineViewPair.getController());
        } catch (ClientException e) {
            final String warningMessage = "Unable to add FileLine element for file=" + encryptionFile.getFile().getPath();
            LOGGER.warn(warningMessage, e);
            AlertUtil.createAndShowWarningAlert(warningMessage);
        }
    }

    private boolean isEncryptionFileNotPresentInLoadedFiles(@NonNull final EncryptionFile encryptionFile) {
        return !this.fileLines.containsKey(encryptionFile);
    }

    private void removeFileLine(@NonNull final EncryptionFile encryptionFile, @NonNull final Parent root) {
        this.fileLines.remove(encryptionFile);
    }
}
