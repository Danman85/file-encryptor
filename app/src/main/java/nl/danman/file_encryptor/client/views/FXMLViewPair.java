package nl.danman.file_encryptor.client.views;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.scene.Parent;

public class FXMLViewPair<C extends Controller> {

    private final Parent root;

    private final C controller;

    public FXMLViewPair(final Parent viewRoot, final C viewController) {
        this.root = viewRoot;
        this.controller = viewController;
    }

    @NonNull
    public Parent getRoot() {
        return root;
    }

    @NonNull
    public C getController() {
        return controller;
    }
}
