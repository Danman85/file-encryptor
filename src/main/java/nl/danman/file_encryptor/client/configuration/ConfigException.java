package nl.danman.file_encryptor.client.configuration;

public class ConfigException extends Exception {

    public ConfigException(final String message) {
        super(message);
    }

    public ConfigException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConfigException(final Throwable cause) {
    }
}
