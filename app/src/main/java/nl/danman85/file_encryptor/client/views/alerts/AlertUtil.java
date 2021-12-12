package nl.danman85.file_encryptor.client.views.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AlertUtil {

    public static void createAndShowBlankAlert(final @Nullable String message,
                                          final @Nullable ButtonType... buttonTypes) {
        var alert = createBlankAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createBlankAlert(final @Nullable String message,
                                         final @Nullable ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.NONE, buttonTypes);
    }

    public static void createAndShowInformationAlert(final @Nullable String message,
                                               final @Nullable ButtonType... buttonTypes) {
        var alert = createInformationAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createInformationAlert(final @Nullable String message,
                                             final @Nullable ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.INFORMATION, buttonTypes);
    }

    public static void createAndShowConformationAlert(final @Nullable String message,
                                                     final @Nullable ButtonType... buttonTypes) {
        var alert = createConfirmationAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createConfirmationAlert(final @Nullable String message,
                                              final @Nullable ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.CONFIRMATION, buttonTypes);
    }

    public static void createAndShowWarningAlert(final @Nullable String message,
                                                     final @Nullable ButtonType... buttonTypes) {
        var alert = createWarningAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createWarningAlert(final @Nullable String message,
                                           final @Nullable ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.WARNING, buttonTypes);
    }

    public static void createAndShowErrorAlert(final @Nullable String message,
                                                     final @Nullable ButtonType... buttonTypes) {
        var alert = createErrorAlert(message, buttonTypes);
        alert.show();
    }

    public static Alert createErrorAlert(final @Nullable String message,
                                       final @Nullable ButtonType... buttonTypes) {
        return createAlert(message, Alert.AlertType.ERROR, buttonTypes);
    }

    private static Alert createAlert(final @Nullable String message,
                                     final @Nonnull Alert.AlertType alertType,
                                     final @Nullable ButtonType... buttonTypes) {
        return new Alert(alertType, message, buttonTypes);
    }
}
