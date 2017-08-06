package de.djuelg.neuronizer.domain.model.todolist;

import java.util.Objects;

import de.djuelg.neuronizer.domain.comparator.PositionCompareable;

/**
 * Created by Domi on 23.07.2017.
 *
 * A TodoListSection contains a Header and it's subitems
 */

public class TodoListSection implements PositionCompareable {

    private final TodoListHeader header;
    private final Iterable<TodoListItem> items;

    public TodoListSection(TodoListHeader header, Iterable<TodoListItem> items) {
        this.header = header;
        this.items = items;
    }

    public TodoListHeader getHeader() {
        return header;
    }

    public Iterable<TodoListItem> getItems() {
        return items;
    }

    @Override
    public int getPosition() {
        return header.getPosition();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListSection)) return false;
        TodoListSection that = (TodoListSection) o;
        return Objects.equals(header, that.header) &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, items);
    }

}
