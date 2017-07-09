package de.djuelg.neuronizer.domain.model;

import java.util.Date;
import java.util.Objects;

import de.djuelg.neuronizer.domain.model.base.TodoListUsable;

/**
 * Created by Domi on 26.03.2017.
 *
 * Model of a TodoList
 */

public class TodoList implements TodoListUsable {

    private final String uuid;
    private final String title;
    private final Date createdAt;
    private final Date changedAt;
    private final int position;

    public TodoList(String uuid, String title, Date createdAt, Date changedAt, int position) {
        this.uuid = Objects.requireNonNull(uuid);
        this.title = Objects.requireNonNull(title);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.changedAt = Objects.requireNonNull(changedAt);
        this.position = Objects.requireNonNull(position);
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getChangedAt() {
        return changedAt;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoList todoList = (TodoList) o;
        return position == todoList.position &&
                Objects.equals(uuid, todoList.uuid) &&
                Objects.equals(title, todoList.title) &&
                Objects.equals(createdAt, todoList.createdAt) &&
                Objects.equals(changedAt, todoList.changedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, createdAt, changedAt, position);
    }
}
