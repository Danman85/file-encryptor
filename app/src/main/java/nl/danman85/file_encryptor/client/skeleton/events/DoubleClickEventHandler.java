package nl.danman85.file_encryptor.client.skeleton.events;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.function.Consumer;

public class DoubleClickEventHandler implements EventHandler<MouseEvent> {

    private final Consumer<MouseEvent> action;

    public DoubleClickEventHandler(final Consumer<MouseEvent> action) {
        this.action = action;
    }

    @Override
    public void handle(MouseEvent event) {
        if (isDoubleClick(event)) {
            this.action.accept(event);
        }
    }

    private boolean isDoubleClick(final MouseEvent mouseEvent) {
        return MouseButton.PRIMARY.equals(mouseEvent.getButton())
                && mouseEvent.getClickCount() == 2;
    }
}
