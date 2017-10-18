package de.djuelg.neuronizer.domain.model.preview;

import android.text.TextUtils;

import com.fernandocejas.arrow.optional.Optional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import de.djuelg.neuronizer.domain.comparator.PositionComparator;
import de.djuelg.neuronizer.domain.model.BaseModel;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;

/**
 * Created by djuelg on 09.07.17.
 *
 * Group of TodoList items to display a preview
 */

public class TodoListPreview implements Preview {

    private final TodoList todoList;
    private final TodoListHeader header;
    private final List<TodoListItem> items;

    public TodoListPreview(TodoList todoList, TodoListHeader header, List<TodoListItem> items) {
        this.todoList = todoList;
        this.header = header;
        this.items = items;
    }

    @Override
    public BaseModel getBaseItem() {
        return todoList;
    }

    @Override
    public String getSubtitle() {
        return Optional.fromNullable(header).or(TodoListHeader.absent()).getTitle();
    }

    @Override
    public String getDetails() {
        Collections.sort(items, new PositionComparator());
        return TextUtils.join(", ", items);
    }

    @Override
    public long calculateImportance() {
        return todoList.calculateImportance();
    }

    @Override
    public int getPosition() {
        return todoList.getPosition();
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
