package be.zsoft.todo.ui.dialog;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.service.TodoItemService;
import be.zsoft.todo.ui.event.TodoItemChangedEvent;
import be.zsoft.todo.ui.service.DialogService;
import be.zsoft.todo.ui.utils.FxmlLoaderUtils;
import be.zsoft.todo.utils.Constants;
import be.zsoft.todo.utils.event.Event;
import be.zsoft.todo.utils.event.EventListener;
import be.zsoft.todo.utils.event.GlobalEventEmitter;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCircleToggleNode;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class TodoItemDetailsDialog extends MFXGenericDialog {

    @FXML
    private Label descriptionLbl;

    @FXML
    private MFXScrollPane descriptionScrollPane;

    @FXML
    private Label dueDateLbl;

    @FXML
    private MFXCircleToggleNode finishedToggle;

    @FXML
    private Label listLbl;

    @FXML
    private MFXButton starredBtn;

    @FXML
    private Label starredLbl;

    @FXML
    private Label titleLbl;

    private TodoItem item;
    private final Pane owner;
    private final TodoItemService todoItemService;
    private final DialogService dialogService;
    private final String globalEventListenerId;

    public TodoItemDetailsDialog(TodoItem item, Pane owner, TodoItemService todoItemService, DialogService dialogService) {
        this.item = item;
        this.owner = owner;
        this.todoItemService = todoItemService;
        this.dialogService = dialogService;

        finishedToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
           onToggleFinished(newValue);
        });

        globalEventListenerId = listenForItemUpdate();
        initUI();
    }

    public void dispose() {
        GlobalEventEmitter.removeListener(globalEventListenerId);
    }

    @FXML
    void onDelete(ActionEvent event) {
        todoItemService.deleteItem(item);
        GlobalEventEmitter.emit(new TodoItemChangedEvent(TodoItemChangedEvent.EventType.REMOVE, item));
        close();
    }

    @FXML
    void onEdit(ActionEvent event) {
        dialogService.createAddUpdateItemDialog(item, owner).showAndWait();

    }

    @FXML
    void onToggleStarred(ActionEvent event) {
        item = todoItemService.toggleStarred(item);
        event.consume();
        initUI();
    }

    @Override
    protected void buildContent() {
        try {
            GridPane gridPane = FxmlLoaderUtils.loadControl("fxml/dialogs/item-details.fxml", this, false);
            setContent(gridPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String listenForItemUpdate() {
        String id = UUID.randomUUID().toString();
        GlobalEventEmitter.registerListener(id, new EventListener() {
            @Override
            public void onEvent(Event event) {
                item = ((TodoItemChangedEvent) event).item();
                initUI();
            }

            @Override
            public boolean supports(Event event) {
                return event instanceof TodoItemChangedEvent &&
                        ((TodoItemChangedEvent)event).item().getId().equals(item.getId());
            }
        });

        return id;
    }

    private void initUI() {
        titleLbl.setText(item.getTitle());
        listLbl.setText(item.getList().getName());
        dueDateLbl.setText(Optional.ofNullable(item.getDueDate()).map(date -> date.format(Constants.DUE_DATE_FORMAT)).orElse("-"));
        starredLbl.setText(item.isStarred() ? "Yes" : "No");
        finishedToggle.setSelected(item.isFinished());

        if (item.isStarred()) {
            MFXFontIcon icon = new MFXFontIcon();
            icon.setIconsProvider(IconsProviders.FONTAWESOME_SOLID);
            icon.setDescription("fas-star");
            icon.setSize(32);
            starredBtn.setGraphic(icon);
        } else {
            MFXFontIcon icon = new MFXFontIcon();
            icon.setIconsProvider(IconsProviders.FONTAWESOME_REGULAR);
            icon.setDescription("far-star");
            icon.setSize(32);
            starredBtn.setGraphic(icon);
        }


        setupDescription();
    }

    private void setupDescription() {
        descriptionLbl.maxWidthProperty().bind(descriptionScrollPane.widthProperty());
        descriptionLbl.maxHeightProperty().bind(descriptionScrollPane.heightProperty());

        descriptionLbl.setText(item.getDescription());
    }

    private void onToggleFinished(boolean state) {
        item = todoItemService.setFinishedState(item, state);
        GlobalEventEmitter.emit(new TodoItemChangedEvent(TodoItemChangedEvent.EventType.UPDATE, item));
        initUI();
    }

    private void close() {
        actions.getChildren().getFirst().fireEvent(new MouseEvent(
                MouseEvent.MOUSE_CLICKED, 0d, 0d, 0d, 0d, MouseButton.PRIMARY, 1,
                false, false, false, false, true,
                false, false, false, false, false, null)
        );
    }
}
