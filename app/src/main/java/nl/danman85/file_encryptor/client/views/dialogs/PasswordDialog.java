package nl.danman85.file_encryptor.client.views.dialogs;

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

    private void toggleOkButtonBasedOnRegex(final KeyEvent event) {
        final String preEventText = this.passwordField.getText();
        final String futureText;
        if ("\b".equals(event.getCharacter())) {
            if (preEventText.length() > 0) {
                futureText = preEventText.substring(0, preEventText.length() -1);
            } else {
                futureText = "";
            }
        } else {
            futureText = preEventText + event.getCharacter();
        }
        final boolean isTextInValidPassword = !futureText.matches(this.passwordRegex);
        this.okButton.setDisable(isTextInValidPassword);
    }
}
