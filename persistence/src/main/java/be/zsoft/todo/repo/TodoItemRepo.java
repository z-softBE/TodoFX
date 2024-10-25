package be.zsoft.todo.repo;

import be.zsoft.todo.model.TodoItem;
import be.zsoft.todo.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoItemRepo extends JpaRepository<TodoItem, String> {

    @Query("SELECT ti FROM TodoItem ti WHERE ti.dueDate = :date  ORDER BY LOWER(ti.title) ASC")
    List<TodoItem> findAllByDueDate(LocalDate date);

    @Query("SELECT ti FROM TodoItem ti WHERE ti.starred = true  ORDER BY LOWER(ti.title) ASC")
    List<TodoItem> findAllStarredItems();

    @Query("SELECT ti FROM TodoItem ti WHERE ti.list = :list  ORDER BY LOWER(ti.title) ASC")
    List<TodoItem> findAllByList(TodoList list);
}
