package be.zsoft.todo.ui.control;

import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.utils.NodeUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class FloatingActionButton extends MFXIconWrapper {

    public FloatingActionButton(String icon, int iconSize, Color iconColor, int wrapperSize, Color backgroundColor, Runnable action) {
        super(icon, iconSize, iconColor, wrapperSize);

        defaultRippleGeneratorBehavior();
        setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        NodeUtils.makeRegionCircular(this);

        addEventHandler(MouseEvent.MOUSE_ENTERED, _ -> getScene().setCursor(Cursor.HAND));
        addEventHandler(MouseEvent.MOUSE_EXITED, _ -> getScene().setCursor(Cursor.DEFAULT));
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;

            action.run();
        });
    }
}
