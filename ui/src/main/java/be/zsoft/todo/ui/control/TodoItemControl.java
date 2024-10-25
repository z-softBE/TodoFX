package be.zsoft.todo.ui.control;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.service.TodoItemService;
import be.zsoft.todo.ui.event.TodoItemChangedEvent;
import be.zsoft.todo.ui.service.DialogService;
import be.zsoft.todo.ui.utils.FxmlLoaderUtils;
import be.zsoft.todo.utils.event.GlobalEventEmitter;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.mfxcore.controls.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.io.IOException;

public class TodoItemControl extends GridPane {

    @FXML
    private MFXCheckbox completedCheck;

    @FXML
    private Label dueDateLbl;

    @FXML
    private Label taskTitleLbl;

    private final Pane dialogRoot;
    private final TodoItemService todoItemService;
    private final DialogService dialogService;
    private final MFXContextMenu contextMenu;

    private TodoItem item;

    public TodoItemControl(TodoItem item, Pane dialogRoot, TodoItemService todoItemService, DialogService dialogService) {
        this.item = item;
        this.dialogRoot = dialogRoot;
        this.todoItemService = todoItemService;
        this.dialogService = dialogService;
        contextMenu = createContextMenu();

        try {
            FxmlLoaderUtils.loadControl("fxml/controls/todo-item.fxml", this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setHeight(80);
        setOnContextMenuRequested(event -> contextMenu.show(this, event.getScreenX(), event.getScreenY()));
    }

    @FXML
    public void initialize() {
        taskTitleLbl.setText(item.getTitle());
        completedCheck.setSelected(item.isFinished());

        if (item.getDueDate() != null) {
            dueDateLbl.setText(item.getDueDate().toString());
        } else {
            dueDateLbl.setText("");
        }

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;

            viewItemDetails();
        });
    }

    @FXML
    void onToggleFinished(ActionEvent event) {
        item = todoItemService.toggleFinished(item);
    }

    private MFXContextMenu createContextMenu() {
        MFXContextMenu contextMenu = new MFXContextMenu(this);

        MFXContextMenuItem viewDetailsItem = new MFXContextMenuItem("View details");
        MFXContextMenuItem updateItem = new MFXContextMenuItem("Update");
        MFXContextMenuItem removeItem = new MFXContextMenuItem("Remove");

        viewDetailsItem.setOnAction(_ -> viewItemDetails());
        updateItem.setOnAction(_ -> dialogService.createAddUpdateItemDialog(item, dialogRoot).showAndWait());
        removeItem.setOnAction(_ -> dialogService.createConfirmDialog(
                "Are you sure?",
                "Are you sure that you want to remove '%s'?".formatted(item.getTitle()),
                dialogRoot,
                () -> {
                    todoItemService.deleteItem(item);
                    GlobalEventEmitter.emit(new TodoItemChangedEvent(TodoItemChangedEvent.EventType.REMOVE, item));
                }
        ).showAndWait());

        contextMenu.addItem(viewDetailsItem);
        contextMenu.addItem(updateItem);
        contextMenu.addLineSeparator(new Line());
        contextMenu.addItem(removeItem);

        return contextMenu;
    }

    private void viewItemDetails() {
        dialogService.createItemDetailsDialog(item, dialogRoot).showAndWait();
    }
}
