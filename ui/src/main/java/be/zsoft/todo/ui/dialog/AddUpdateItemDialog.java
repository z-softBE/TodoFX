package be.zsoft.todo.ui.dialog;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.model.TodoList;
import be.zsoft.todo.service.dto.TodoItemRequest;
import be.zsoft.todo.ui.utils.FxmlLoaderUtils;
import io.github.palexdev.materialfx.beans.NumberRange;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.utils.others.dates.DateStringConverter;
import io.github.palexdev.mfxcore.utils.StringUtils;
import io.github.palexdev.mfxcore.utils.converters.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.time.Year;
import java.util.List;

public class AddUpdateItemDialog extends MFXGenericDialog {

    @FXML
    private TextArea descriptionTxt;

    @FXML
    private MFXDatePicker dueDatePicker;

    @FXML
    private MFXTextField taskTxt;

    @FXML
    private MFXFilterComboBox<TodoList> todoListCb;

    public AddUpdateItemDialog(TodoItem item, List<TodoList> lists) {
        super(item != null ? "Update TODO item" : "Create TODO item", "");

        showMinimizeProperty().set(false);
        alwaysOnTopProperty().set(true);
        showAlwaysOnTopProperty().set(true);

        setupListCb(lists);
        setupDatePicker();

        if (item != null) {
            fillInData(item);
        }
    }

    public TodoItemRequest getRequestData() {
        return  new TodoItemRequest(
                taskTxt.getText(),
                descriptionTxt.getText(),
                dueDatePicker.getValue(),
                todoListCb.getSelectedItem()
        );
    }

    @Override
    protected void buildContent() {
        try {
            GridPane gridPane = FxmlLoaderUtils.loadControl("fxml/dialogs/add-item.fxml", this, false);
            setContent(gridPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupListCb(List<TodoList> lists) {
        todoListCb.getItems().clear();
        todoListCb.setItems(FXCollections.observableArrayList(lists));
        todoListCb.selectItem(todoListCb.getItems().stream().filter(list -> list.getName().equals("Default")).findFirst().orElse(null));

        todoListCb.setConverter(FunctionalStringConverter.to(list -> list != null ? list.getName() : ""));
        todoListCb.setFilterFunction(search -> list -> StringUtils.containsIgnoreCase(list.getName(), search));
    }

    private void setupDatePicker() {
        dueDatePicker.setConverterSupplier(() -> new DateStringConverter("dd/MM/yyyy", dueDatePicker.getLocale()));
        dueDatePicker.setYearsRange(new NumberRange<>(Year.now().getValue(), 2150));
    }

    private void fillInData(TodoItem item) {
        taskTxt.setText(item.getTitle());
        descriptionTxt.setText(item.getDescription());
        dueDatePicker.setValue(item.getDueDate());
    }
}
