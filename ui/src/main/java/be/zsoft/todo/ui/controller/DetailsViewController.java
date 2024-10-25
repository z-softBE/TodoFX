package be.zsoft.todo.ui.controller;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.service.TodoItemService;
import be.zsoft.todo.ui.control.FloatingActionButton;
import be.zsoft.todo.ui.control.TodoItemControl;
import be.zsoft.todo.ui.event.TodoItemChangedEvent;
import be.zsoft.todo.ui.event.TodoListChangedEvent;
import be.zsoft.todo.ui.service.DialogService;
import be.zsoft.todo.ui.utils.navbar.NavbarItem;
import be.zsoft.todo.utils.event.Event;
import be.zsoft.todo.utils.event.EventListener;
import be.zsoft.todo.utils.event.GlobalEventEmitter;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetailsViewController {

    @Autowired
    private DialogService dialogService;

    @Autowired
    private TodoItemService todoItemService;

    @FXML
    private AnchorPane fabPane;

    @FXML
    private StackPane contentPane;

    @FXML
    private VBox todoItemsVBox;

    private NavbarItem currentNavbarItem;

    @FXML
    private void initialize() {
        createFAB();
        setupGlobalListener();
    }

    public void initController(NavbarItem navbarItem, String scrollToItemId) {
        currentNavbarItem = navbarItem;

        List<TodoItem> items = switch (navbarItem.type()) {
            case TODAY -> todoItemService.getAllItemsDueToday();
            case STARRED -> todoItemService.getAllStarredItems();
            case DB -> todoItemService.getAllItemsByList(navbarItem.list());
        };

        todoItemsVBox.getChildren().clear();
        items.forEach(item -> todoItemsVBox.getChildren().add(new TodoItemControl(item, contentPane, todoItemService, dialogService)));

        if (scrollToItemId != null) {
            // TODO: Scrolling to correct item
        }
    }

    private void createFAB() {
        FloatingActionButton fab = new FloatingActionButton(
                "fas-plus",
                32,
                Color.WHITE,
                64,
                Color.rgb(0,149,200),
                () -> dialogService.createAddUpdateItemDialog(null, contentPane).showAndWait()
        );

        AnchorPane.setBottomAnchor(fab, 15.0);
        AnchorPane.setRightAnchor(fab, 15.0);

        fabPane.getChildren().add(fab);
    }

    private void setupGlobalListener() {
        GlobalEventEmitter.registerListener(DetailsViewController.class.getName(), new EventListener() {
            @Override
            public void onEvent(Event event) {
                TodoItemChangedEvent realEvent = (TodoItemChangedEvent) event;
                initController(currentNavbarItem, realEvent.item().getId());
            }

            @Override
            public boolean supports(Event event) {
                return event instanceof TodoItemChangedEvent;
            }
        });
    }
}
