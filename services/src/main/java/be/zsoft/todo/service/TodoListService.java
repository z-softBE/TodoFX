package be.zsoft.todo.service;

import be.zsoft.todo.model.TodoList;
import be.zsoft.todo.repo.TodoListRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor

@Service
public class TodoListService {

    private final TodoListRepo todoListRepo;

    public List<TodoList> getAll() {
        return todoListRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public TodoList createTodoList(String name) {
        TodoList list = TodoList.builder().name(name).build();

        return todoListRepo.saveAndFlush(list);
    }

    public TodoList updateName(TodoList list, String name) {
        list.setName(name);

        return todoListRepo.saveAndFlush(list);
    }

    public void deleteTodoList(TodoList list) {
        todoListRepo.delete(list);
    }

}
