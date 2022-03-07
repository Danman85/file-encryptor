module nl.danman.file_encryptor {

    requires org.apache.logging.log4j;
    requires com.github.spotbugs.annotations;

    requires javafx.controls;
    requires javafx.fxml;

    requires nl.danman.file_encryptor.service;

    opens nl.danman.file_encryptor.client.views.main to javafx.fxml;
    opens nl.danman.file_encryptor.client.views.file_encryptor to javafx.fxml;

    exports nl.danman.file_encryptor;
}