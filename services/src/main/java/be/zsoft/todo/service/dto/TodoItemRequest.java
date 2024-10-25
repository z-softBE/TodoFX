package be.zsoft.todo.service.dto;

import be.zsoft.todo.model.TodoList;

import java.time.LocalDate;

public record TodoItemRequest(
        String title,
        String description,
        LocalDate dueDate,
        TodoList list
) {
}
