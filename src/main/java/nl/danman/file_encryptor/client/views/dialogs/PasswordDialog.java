package nl.danman.file_encryptor.client.views.dialogs;

import edu.umd.cs.findbugs.annotations.NonNull;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class PasswordDialog extends Dialog<String> {

    public static final String PASSWORD_REQUIREMENTS = "Password must adhere to the following:\n" +
            "- At least 8 characters;\n" +
            "- Must contain: 1 uppercase letter, 1 lowercase letter and 1 number;\n" +
            "- It may contain special characters.";

    private final String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"; // default

    private final HBox contentHBox = new HBox();
    private final PasswordField passwordField = new PasswordField();
    private Button okButton;
    private Button cancelButton;

    public PasswordDialog(final String headerText,
                          final String contentText) {
        setHeaderText(headerText);
        setContentText(contentText);
        init();
    }

    private void init() {
        initLayout();
        setupBehaviour();
    }

    private void initLayout() {
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        this.cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        this.passwordField.setTooltip(new Tooltip(PASSWORD_REQUIREMENTS));
        this.contentHBox.getChildren().addAll(
                new Label("Password:"),
                this.passwordField);
        this.contentHBox.setSpacing(10);
        getDialogPane().setContent(this.contentHBox);
        this.passwordField.requestFocus();
    }

    private void setupBehaviour() {
        this.okButton.setDisable(true);
        this.passwordField.addEventFilter(KeyEvent.KEY_TYPED, this::toggleOkButtonBasedOnRegex);
        setResultConverter(buttonType -> this.passwordField.getText());
    }

    private void toggleOkButtonBasedOnRegex(@NonNull final KeyEvent event) {
        final String futureText = determineNewString(event.getCharacter());
        this.okButton.setDisable(!futureText.matches(this.passwordRegex));
    }

    @NonNull
    private String determineNewString(@NonNull final String eventCharacter) {
        final String preEventText = this.passwordField.getText();
        if ("\b".equals(eventCharacter)) {
            if (preEventText.length() > 0) {
                return preEventText.substring(0, preEventText.length() -1);
            } else {
                return "";
            }
        } else {
            return preEventText + eventCharacter;
        }
    }
}
