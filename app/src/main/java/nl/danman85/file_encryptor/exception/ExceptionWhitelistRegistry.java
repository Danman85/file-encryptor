package nl.danman85.file_encryptor.exception;

/**
 * Registry for the entire app.
 *
 * Since it is way less work to register the exceptional cases, in which
 * the application does NOT have to terminate, we just register the
 * exceptions that can be white-flagged.
 */
public final class ExceptionWhitelistRegistry {

    private ExceptionWhitelistRegistry() {
        // hide me
    }

    public static final Class<? extends Exception>[] WHITE_FLAGGED = new Class[] {
    };
}
