package be.zsoft.todo.ui.service;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.model.TodoList;
import be.zsoft.todo.service.TodoItemService;
import be.zsoft.todo.service.TodoListService;
import be.zsoft.todo.ui.dialog.AddUpdateItemDialog;
import be.zsoft.todo.ui.dialog.AddUpdateListDialog;
import be.zsoft.todo.ui.dialog.TodoItemDetailsDialog;
import be.zsoft.todo.ui.event.TodoItemChangedEvent;
import be.zsoft.todo.ui.event.TodoListChangedEvent;
import be.zsoft.todo.ui.utils.StageHolder;
import be.zsoft.todo.utils.event.GlobalEventEmitter;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor

@Service
public class DialogService {

    private final TodoListService todoListService;
    private final TodoItemService todoItemService;

    public MFXStageDialog createAddListDialog(Pane owner) {
        AddUpdateListDialog addUpdateListDialog = new AddUpdateListDialog(null);

        MFXStageDialog dialog = createDialog(addUpdateListDialog, "Add a new TODO list", owner);

        addUpdateListDialog.addActions(
                Map.entry(new MFXButton("Save"), _ -> {
                    String name = ((AddUpdateListDialog) dialog.getContent()).getInput();
                    TodoList todoList = todoListService.createTodoList(name);
                    GlobalEventEmitter.emit(new TodoListChangedEvent(TodoListChangedEvent.EventType.ADD, todoList.getId()));
                    dialog.close();
                }),
                Map.entry(new MFXButton("Cancel"), _ -> dialog.close())
        );

        return dialog;
    }

    public MFXStageDialog createUpdateListDialog(TodoList list, Pane owner) {
        AddUpdateListDialog addUpdateListDialog = new AddUpdateListDialog(list);

        MFXStageDialog dialog = createDialog(addUpdateListDialog, "Update a TODO list", owner);

        addUpdateListDialog.addActions(
                Map.entry(new MFXButton("Save"), _ -> {
                    String name = ((AddUpdateListDialog) dialog.getContent()).getInput();
                    TodoList todoList = todoListService.updateName(list, name);
                    GlobalEventEmitter.emit(new TodoListChangedEvent(TodoListChangedEvent.EventType.UPDATE, todoList.getId()));
                    dialog.close();
                }),
                Map.entry(new MFXButton("Cancel"), _ -> dialog.close())
        );

        return dialog;
    }

    public MFXStageDialog createConfirmDialog(String title, String content, Pane owner, Runnable onConfirm) {
        MFXGenericDialog dialogContent = MFXGenericDialogBuilder.build()
                .setContentText(content)
                .makeScrollable(true)
                .get();

        MFXStageDialog dialog = createDialog(dialogContent, title, owner);
        dialogContent.addActions(
                Map.entry(new MFXButton("Confirm"), _ -> {
                    onConfirm.run();
                    dialog.close();
                }),
                Map.entry(new MFXButton("Cancel"), _ -> dialog.close())
        );

        return dialog;
    }

    public MFXStageDialog createAddUpdateItemDialog(TodoItem item, Pane owner) {
        AddUpdateItemDialog dialogContent = new AddUpdateItemDialog(item, todoListService.getAll());

        MFXStageDialog dialog = createDialog(dialogContent, item != null ? "Update a TODO item" : "Add a new TODO item", owner);

        dialogContent.addActions(
                Map.entry(new MFXButton("Save"), _ -> {
                    if (item == null) {
                        TodoItem savedItem = todoItemService.createItem(dialogContent.getRequestData());
                        GlobalEventEmitter.emit(new TodoItemChangedEvent(TodoItemChangedEvent.EventType.ADD, savedItem));
                    } else {
                        TodoItem savedItem = todoItemService.updateItem(item, dialogContent.getRequestData());
                        GlobalEventEmitter.emit(new TodoItemChangedEvent(TodoItemChangedEvent.EventType.UPDATE, savedItem));
                    }

                    dialog.close();
                }),
                Map.entry(new MFXButton("Cancel"), _ -> dialog.close())
        );

        return dialog;
    }

    public MFXStageDialog createItemDetailsDialog(TodoItem item, Pane owner) {
        TodoItemDetailsDialog dialogContent = new TodoItemDetailsDialog(item, owner, todoItemService, this);
        MFXStageDialog dialog = createDialog(dialogContent, "TODO Item details", owner);

        dialogContent.addActions(Map.entry(new MFXButton("Ok"), _ -> {
            dialogContent.dispose();
            dialog.close();
        }));

        return dialog;
    }

    private MFXStageDialog createDialog(MFXGenericDialog dialog, String title, Pane owner) {
        return MFXGenericDialogBuilder.build(dialog)
                .toStageDialogBuilder()
                .initOwner(StageHolder.getPrimaryStage())
                .initModality(Modality.APPLICATION_MODAL)
                .setDraggable(true)
                .setTitle(title)
                .setOwnerNode(owner)
                .setScrimPriority(ScrimPriority.WINDOW)
                .setScrimOwner(true)
                .get();
    }

}
