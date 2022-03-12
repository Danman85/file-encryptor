package nl.danman.file_encryptor.client.views;

import nl.danman.file_encryptor.client.views.file_encryptor.FileEncryptorController;
import nl.danman.file_encryptor.client.views.file_encryptor.FileLineController;
import nl.danman.file_encryptor.client.views.main.MainViewController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FXMLViewSourceTests {

    @Test
    void mainViewIsDefined() {
        // given
        final FXMLViewDefinition mainView = FXMLViewDefinition.MAIN_VIEW;

        // then
        assertEquals("/views/MainView.fxml", mainView.getResourceUrl());
        assertEquals(MainViewController.class, mainView.getClazz());
    }

    @Test
    void fileEncryptorViewIsDefined() {
        // given
        final FXMLViewDefinition fileEncryptorView = FXMLViewDefinition.FILE_ENCRYPTOR;

        // then
        assertEquals("/views/file_encryptor/FileEncryptor.fxml", fileEncryptorView.getResourceUrl());
        assertEquals(FileEncryptorController.class, fileEncryptorView.getClazz());
    }

    @Test
    void fileLineViewIsDefined() {
        // given
        final FXMLViewDefinition fileLineView = FXMLViewDefinition.FILE_LINE;

        // then
        assertEquals("/views/file_encryptor/FileLine.fxml", fileLineView.getResourceUrl());
        assertEquals(FileLineController.class, fileLineView.getClazz());
    }
}
