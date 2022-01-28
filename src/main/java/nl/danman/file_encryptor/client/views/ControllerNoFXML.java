package nl.danman.file_encryptor.client.views;

import edu.umd.cs.findbugs.annotations.NonNull;

public abstract class ControllerNoFXML<V extends ViewNoFXML> implements Controller {

    private final V view;

    private ControllerNoFXML(final V view) {
        this.view = view;
    }

    public abstract void createView(final @NonNull V view);

    public abstract void setupBehaviour();

    private V getView() {
        return view;
    }
}
