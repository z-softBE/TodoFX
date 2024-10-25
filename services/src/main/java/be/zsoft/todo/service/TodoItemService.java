package be.zsoft.todo.service;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.model.TodoList;
import be.zsoft.todo.repo.TodoItemRepo;
import be.zsoft.todo.service.dto.TodoItemRequest;
import be.zsoft.todo.service.mapper.TodoItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Slf4j

@Service
public class TodoItemService {

    private final TodoItemRepo todoItemRepo;
    private final TodoItemMapper todoItemMapper;

    public TodoItem createItem(TodoItemRequest request) {
        TodoItem item = todoItemMapper.fromRequest(request);

        return todoItemRepo.saveAndFlush(item);
    }

    public TodoItem updateItem(TodoItem item, TodoItemRequest request) {
        item = todoItemMapper.fromRequest(request, item);

        return todoItemRepo.saveAndFlush(item);
    }

    public List<TodoItem> getAllItemsDueToday() {
        LocalDate today = LocalDate.now();
        return todoItemRepo.findAllByDueDate(today);
    }

    public List<TodoItem> getAllStarredItems() {
        return todoItemRepo.findAllStarredItems();
    }

    public List<TodoItem> getAllItemsByList(TodoList list) {
        return todoItemRepo.findAllByList(list);
    }

    public TodoItem toggleStarred(TodoItem item) {
        item.setStarred(!item.isStarred());

        return todoItemRepo.saveAndFlush(item);
    }

    public TodoItem toggleFinished(TodoItem item) {
        item.setFinished(!item.isFinished());

        return todoItemRepo.saveAndFlush(item);
    }

    public void deleteItem(TodoItem item) {
        todoItemRepo.delete(item);
    }

    public TodoItem setFinishedState(TodoItem item, boolean state) {
        item.setFinished(state);

        return todoItemRepo.saveAndFlush(item);
    }
}
