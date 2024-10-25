package be.zsoft.todo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "todo_items")
public class TodoItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "starred", nullable = false)
    private boolean starred;

    @Column(name = "finished", nullable = false)
    private boolean finished;

    @Column(name = "due_date", nullable = true)
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "todo_list_id", nullable = false)
    private TodoList list;
}
