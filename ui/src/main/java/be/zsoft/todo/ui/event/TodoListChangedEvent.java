package be.zsoft.todo.ui.event;

import be.zsoft.todo.utils.event.Event;

public record TodoListChangedEvent(EventType type, String listId) implements Event {
    public enum EventType {
        ADD,
        UPDATE,
        REMOVE
    }
}
