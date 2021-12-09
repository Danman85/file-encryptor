package nl.danman85.file_encryptor.exception;

/**
 * This exception is thrown when an action with resource fails
 */
public class ResourceException extends Exception {

    public ResourceException(final String message) {
        super(message);
    }

    public ResourceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
