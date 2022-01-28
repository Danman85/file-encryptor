package nl.danman.file_encryptor.client.configuration;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Objects;

public class ConfigValidator {

    private static final Logger LOGGER = LogManager.getLogger(ConfigValidator.class);
    public static final String FXML_EXTENSION = ".fxml";

    public void validate() throws ConfigException {
        LOGGER.info("Validating Configuration");
        validateFXMLResources();
    }

    private void validateFXMLResources() throws ConfigException {
        LOGGER.debug("Validating FXML resources");
        for (final FXMLViewSource resource : FXMLViewSource.values()) {
            File resourceFile;
            try {
                resourceFile = new File(Objects.requireNonNull(getClass().getResource(resource.getResourceUrl())).toURI());
            } catch (Exception e) {
                throw new ConfigException(e);
            }
            validateExistence(resourceFile);
        }
    }

    private void validateExistence(@NonNull final File resourceFile) throws ConfigException {
        if (resourceFile.exists() && resourceFile.getPath().endsWith(FXML_EXTENSION)) {
            LOGGER.debug("Successfully validated view=" + resourceFile.getPath());
        } else {
            final String message = "Resource file:" + resourceFile.getPath() + " does not exist";
            LOGGER.warn(message);
            throw new ConfigException(message);
        }
    }
}
