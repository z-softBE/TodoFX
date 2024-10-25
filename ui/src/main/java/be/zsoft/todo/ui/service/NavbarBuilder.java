package be.zsoft.todo.ui.service;

import be.zsoft.todo.service.TodoListService;
import be.zsoft.todo.ui.event.TodoListChangedEvent;
import be.zsoft.todo.ui.utils.navbar.NavbarItem;
import be.zsoft.todo.ui.utils.navbar.NavbarItemType;
import be.zsoft.todo.utils.event.GlobalEventEmitter;
import io.github.palexdev.materialfx.controls.MFXIconWrapper;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor

@Service
public class NavbarBuilder {

    private final TodoListService todoListService;
    private final DialogService dialogService;

    public List<Node> buildNavbar(ToggleGroup group, Pane owner) {
        List<Node> navbarItems = new ArrayList<>();
        navbarItems.add(createToggle(new NavbarItem(NavbarItemType.TODAY, null), group, owner));
        navbarItems.add(createToggle(new NavbarItem(NavbarItemType.STARRED, null), group, owner));
        navbarItems.add(new Separator(Orientation.HORIZONTAL));

        List<ToggleButton> dbItems = todoListService.getAll().stream()
                .map(item -> createToggle(new NavbarItem(NavbarItemType.DB, item), group, owner))
                .toList();

        navbarItems.addAll(dbItems);

        return navbarItems;
    }

    private ToggleButton createToggle(NavbarItem item, ToggleGroup group, Pane owner) {
        MFXIconWrapper wrapper = new MFXIconWrapper(item.getIcon(), 24, 32);
        MFXRectangleToggleNode toggleNode = new MFXRectangleToggleNode(item.getText(), wrapper);
        toggleNode.setAlignment(Pos.CENTER_LEFT);
        toggleNode.setMaxWidth(Double.MAX_VALUE);
        toggleNode.setToggleGroup(group);
        toggleNode.setUserData(item);
        toggleNode.setContextMenu(buildContextMenu(item, owner));

        return toggleNode;
    }

    public Optional<Toggle> getToggleToSelect(String todoListId, List<Node> navbarItems) {
        return navbarItems.stream()
                .filter(node -> node instanceof ToggleButton)
                .map(node -> (Toggle) node)
                .filter(toggleButton -> toggleButton.getUserData() != null)
                .filter(toggleButton -> shouldNavbarItemBeSelected(((NavbarItem) toggleButton.getUserData()), todoListId))
                .findFirst();
    }

    private ContextMenu buildContextMenu(NavbarItem item, Pane owner) {
        boolean shouldDisableItems = item.type() != NavbarItemType.DB;
        MenuItem renameItem = new MenuItem("Rename");
        MenuItem removeItem = new MenuItem("Remove");

        if (!shouldDisableItems) {
            renameItem.setOnAction(_ -> dialogService.createUpdateListDialog(item.list(), owner).showAndWait());
            removeItem.setOnAction(_ -> dialogService.createConfirmDialog(
                    "Are you sure?",
                    "Are you sure you want to remove TODO list: \"%s\"".formatted(item.list().getName()),
                    owner,
                    () -> {
                        todoListService.deleteTodoList(item.list());
                        GlobalEventEmitter.emit(new TodoListChangedEvent(TodoListChangedEvent.EventType.REMOVE, null));
                    }
            ).showAndWait());
        }

        return new ContextMenu(renameItem, removeItem);
    }

    private boolean shouldNavbarItemBeSelected(NavbarItem navbarItem, String todoListId) {
        if (navbarItem.type() != NavbarItemType.DB || navbarItem.list() == null) {
            return false;
        }

        return navbarItem.list().getId().equals(todoListId);
    }
}
