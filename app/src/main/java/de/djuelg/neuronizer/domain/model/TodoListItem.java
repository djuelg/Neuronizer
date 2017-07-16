package de.djuelg.neuronizer.domain.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import de.djuelg.neuronizer.domain.model.base.TodoListUsable;

/**
 * Created by Domi on 26.03.2017.
 *
 * Model of a TodoListItem which knows the UUIDs of it's parents
 */
public class TodoListItem implements TodoListUsable {

    // TODO add field "isDone"
    private final String uuid;
    private final String title;
    private final Date createdAt;
    private final Date changedAt;
    private final int position;
    private final Deadline deadline;
    private final boolean important;
    private final String details;
    private final String parentTodoListUuid;
    private final String parentHeaderUuid;

    public TodoListItem(String title, int position, Deadline deadline, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        this(UUID.randomUUID().toString(), title, new Date(), new Date(), position, deadline, important, details, parentTodoListUuid, parentHeaderUuid);
    }

    public TodoListItem(String uuid, String title, Date createdAt, Date changedAt, int position, Deadline deadline, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        this.uuid = Objects.requireNonNull(uuid);
        this.title = Objects.requireNonNull(title);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.changedAt = Objects.requireNonNull(changedAt);
        this.position = Objects.requireNonNull(position);
        this.deadline = Objects.requireNonNull(deadline);
        this.important = Objects.requireNonNull(important);
        this.details = Objects.requireNonNull(details);
        this.parentTodoListUuid = Objects.requireNonNull(parentTodoListUuid);
        this.parentHeaderUuid = Objects.requireNonNull(parentHeaderUuid);
    }

    public TodoListItem update(String title, int position, Deadline deadline, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        return new TodoListItem(uuid, title, createdAt, new Date(), position, deadline, important, details, parentTodoListUuid, parentHeaderUuid);
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

    public Deadline getDeadline() {
        return deadline;
    }

    public boolean isImportant() {
        return important;
    }

    public String getDetails() {
        return details;
    }

    public String getParentTodoListUuid() {
        return parentTodoListUuid;
    }

    public String getParentHeaderUuid() {
        return parentHeaderUuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TodoListItem that = (TodoListItem) o;
        return position == that.position &&
                important == that.important &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(title, that.title) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(changedAt, that.changedAt) &&
                Objects.equals(deadline, that.deadline) &&
                Objects.equals(details, that.details) &&
                Objects.equals(parentTodoListUuid, that.parentTodoListUuid) &&
                Objects.equals(parentHeaderUuid, that.parentHeaderUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, createdAt, changedAt, position, deadline, important, details, parentTodoListUuid, parentHeaderUuid);
    }

    @Override
    public String toString() {
        return "TodoListItem{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", changedAt=" + changedAt +
                ", position=" + position +
                ", deadline=" + deadline +
                ", important=" + important +
                ", details='" + details + '\'' +
                ", parentTodoListUuid='" + parentTodoListUuid + '\'' +
                ", parentHeaderUuid='" + parentHeaderUuid + '\'' +
                '}';
    }


}