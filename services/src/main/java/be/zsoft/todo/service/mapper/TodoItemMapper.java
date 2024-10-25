package be.zsoft.todo.service.mapper;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.service.dto.TodoItemRequest;
import org.springframework.stereotype.Component;

@Component
public class TodoItemMapper {

    public TodoItem fromRequest(TodoItemRequest request) {
        return fromRequest(request, new TodoItem());
    }

    public TodoItem fromRequest(TodoItemRequest request, TodoItem item) {
        item.setTitle(request.title());
        item.setDescription(request.description());
        item.setDueDate(request.dueDate());
        item.setList(request.list());
        item.setStarred(item.isStarred());
        item.setFinished(item.isFinished());

        return item;
    }
}
