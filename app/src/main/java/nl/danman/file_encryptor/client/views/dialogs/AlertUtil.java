package nl.danman.file_encryptor.client.views.dialogs;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertUtil {

    public static void createAndShowBlankAlert(@NonNull final String message,
                                          @NonNull final ButtonType... buttonTypes) {
        var alert = createBlankAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createBlankAlert(@NonNull final String message,
                                         @NonNull final ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.NONE, buttonTypes);
    }

    public static void createAndShowInformationAlert(@NonNull final String message,
                                               @NonNull final ButtonType... buttonTypes) {
        var alert = createInformationAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createInformationAlert(@NonNull final String message,
                                             @NonNull final ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.INFORMATION, buttonTypes);
    }

    public static void createAndShowConformationAlert(@NonNull final String message,
                                                     @NonNull final ButtonType... buttonTypes) {
        var alert = createConfirmationAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createConfirmationAlert(@NonNull final String message,
                                              @NonNull final ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.CONFIRMATION, buttonTypes);
    }

    public static void createAndShowWarningAlert(@NonNull final String message,
                                                     @NonNull final ButtonType... buttonTypes) {
        var alert = createWarningAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createWarningAlert(@NonNull final String message,
                                           @NonNull final ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.WARNING, buttonTypes);
    }

    public static void createAndShowErrorAlert(@NonNull final String message,
                                                     @NonNull final ButtonType... buttonTypes) {
        var alert = createErrorAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createErrorAlert(@NonNull final String message,
                                       @NonNull final ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.ERROR, buttonTypes);
    }

    private static Alert createAlert(@NonNull final String message,
                                     @NonNull final Alert.AlertType alertType,
                                     @NonNull final ButtonType... buttonTypes) {
        return new Alert(alertType, message, buttonTypes);
    }
}
