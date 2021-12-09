package nl.danman85.file_encryptor.client.views.main;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML private BorderPane root;
    @FXML private MenuBar menuBar;

    public MainController() {
    }

    public BorderPane getRoot() {
        return root;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }
}
