package be.zsoft.todo.ui.dialog;

import be.zsoft.todo.model.TodoList;
import be.zsoft.todo.ui.utils.FxmlLoaderUtils;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class AddUpdateListDialog extends MFXGenericDialog {

    @FXML
    private MFXTextField nameTxt;

    public AddUpdateListDialog(TodoList list) {
        super(list != null ? "Update a TODO list" : "Add a new list", "");

        showMinimizeProperty().set(false);
        alwaysOnTopProperty().set(true);
        showAlwaysOnTopProperty().set(true);

        if (list != null) {
            nameTxt.setText(list.getName());
        }
    }

    public String getInput() {
        return nameTxt.getText();
    }

    @Override
    protected void buildContent() {
        try {
            GridPane gridPane = FxmlLoaderUtils.loadControl("fxml/dialogs/add-list.fxml", this, false);
            setContent(gridPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
