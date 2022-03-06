module nl.danman.file_encryptor {

    requires org.apache.logging.log4j;
    requires org.apache.commons.io;
    requires com.github.spotbugs.annotations;

    requires javafx.controls;
    requires javafx.fxml;

    opens nl.danman.file_encryptor.client.views.main to javafx.fxml;
    opens nl.danman.file_encryptor.client.views.file_encryptor to javafx.fxml;

    exports nl.danman.file_encryptor;
}