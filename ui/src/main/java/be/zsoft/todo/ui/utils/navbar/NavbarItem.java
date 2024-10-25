package be.zsoft.todo.ui.utils.navbar;

import be.zsoft.todo.model.TodoList;

public record NavbarItem(NavbarItemType type, TodoList list) {

    public String getIcon() {
        return switch (type) {
            case DB -> "fas-list-ul";
            case TODAY -> "fas-sun";
            case STARRED -> "fas-star";
        };
    }

    public String getText() {
        return switch (type) {
            case DB -> list.getName();
            case TODAY -> "Today";
            case STARRED -> "Starred";
        };
    }
}
