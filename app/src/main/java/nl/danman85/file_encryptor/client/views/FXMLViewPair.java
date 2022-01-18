package nl.danman85.file_encryptor.client.views;

import javafx.scene.Parent;

public class FXMLViewPair<C extends Controller> {

    private final Parent root;

    private final C controller;

    public FXMLViewPair(final Parent viewRoot, final C viewController) {
        this.root = viewRoot;
        this.controller = viewController;
    }

    public Parent getRoot() {
        return root;
    }

    public C getController() {
        return controller;
    }
}
