package nl.danman85.file_encryptor.client.views;

public abstract class ControllerNoFXML<V extends ViewNoFXML> implements Controller {

    private final V view;

    private ControllerNoFXML(final V view) {
        this.view = view;
    }

    public abstract void createView(final V view);

    public abstract void setupBehaviour();

    private V getView() {
        return view;
    }
}
