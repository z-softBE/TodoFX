package be.zsoft.todo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "todo_lists")
public class TodoList {

    @Id
    @GeneratedValue(generator = "uuid2")
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "removable", nullable = false)
    private boolean removable;

    @Column(name = "editable", nullable = false)
    private boolean editable;
}
