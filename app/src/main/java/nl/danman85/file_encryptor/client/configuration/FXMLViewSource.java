package nl.danman85.file_encryptor.client.configuration;

public enum FXMLViewSource {

    MAIN_VIEW("/views/MainView.fxml"),
    MENU_BAR("/views/MenuBar.fxml");

    private final String resourceUrl;

    private FXMLViewSource(final String url) {
        this.resourceUrl = url;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }
}
