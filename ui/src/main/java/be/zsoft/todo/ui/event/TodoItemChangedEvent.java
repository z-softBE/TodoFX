package be.zsoft.todo.ui.event;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.utils.event.Event;

public record TodoItemChangedEvent(EventType type, TodoItem item) implements Event {
    public enum EventType {
        ADD,
        UPDATE,
        REMOVE
    }
}
