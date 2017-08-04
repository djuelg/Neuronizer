package de.djuelg.neuronizer.domain.model.preview;

import com.fernandocejas.arrow.optional.Optional;

import java.util.Objects;

import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;

/**
 * Created by djuelg on 09.07.17.
 *
 * Group of TodoList items to display a preview
 */

public class TodoListPreview {

    private final TodoList todoList;
    private final TodoListHeader header;
    private final Iterable<TodoListItem> items;

    public TodoListPreview(TodoList todoList, TodoListHeader header, Iterable<TodoListItem> items) {
        this.todoList = todoList;
        this.header = header;
        this.items = items;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public Optional<TodoListHeader> getHeader() {
        return Optional.fromNullable(header);
    }

    public Iterable<TodoListItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TodoListPreview that = (TodoListPreview) o;
        return Objects.equals(todoList, that.todoList) &&
                Objects.equals(header, that.header) &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoList, header, items);
    }

    @Override
    public String toString() {
        return "TodoListPreview{" +
                "todoList=" + todoList +
                ", header=" + header +
                ", items=" + items +
                '}';
    }
}
