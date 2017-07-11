package de.djuelg.neuronizer.domain.model;

import java.util.Objects;

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

    // TODO evaluate alternative to null
    public TodoListPreview(TodoList todoList) {
        this.todoList = todoList;
        this.header = null;
        this.items = null;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public TodoListHeader getHeader() {
        return header;
    }

    public Iterable<TodoListItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoListPreview that = (TodoListPreview) o;
        return Objects.equals(todoList, that.todoList) &&
                Objects.equals(header, that.header) &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoList, header, items);
    }
}
