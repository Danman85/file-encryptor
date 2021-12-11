package nl.danman85.file_encryptor.client;

/**
 * Exception to be thrown from client code.
 */
public class ClientException extends Exception {

    public ClientException(final String message) {
        super(message);
    }

    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ClientException(final Throwable cause) {
        super(cause);
    }
}
