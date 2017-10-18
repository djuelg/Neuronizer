package de.djuelg.neuronizer.domain.model.preview;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.djuelg.neuronizer.domain.model.BaseModel;

/**
 * Created by Domi on 26.03.2017.
 *
 * Model of a TodoList
 */

public class TodoList implements BaseModel, ImportanceComparable {

    private static final int INCREASE = 1;
    private static final int NORMALIZE = 2;

    private final String uuid;
    private final String title;
    private final Date createdAt;
    private final Date changedAt;
    private final int position;
    private final long accessCounter;

    // constructor for first time creation
    public TodoList(String title, int position) {
        this(UUID.randomUUID().toString(), title, new Date(), new Date(), position, 0L);
    }

    // constructor for model updates / read from database
    public TodoList(String uuid, String title, Date createdAt, Date changedAt, int position, long accessCounter) {
        this.uuid = Objects.requireNonNull(uuid);
        this.title = Objects.requireNonNull(title);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.changedAt = Objects.requireNonNull(changedAt);
        this.position = Objects.requireNonNull(position);
        this.accessCounter = Objects.requireNonNull(accessCounter);
    }

    public TodoList update(String title, int position) {
        if (this.equals(new TodoList(uuid, title, createdAt, changedAt, position, accessCounter))) return this;
        return new TodoList(uuid, title, createdAt, changedAt, position, accessCounter);
    }

    public TodoList updateLastChange() {
        return new TodoList(uuid, title, createdAt, new Date(), position, accessCounter);
    }

    @Override
    public TodoList increaseAccessCounter() {
        return new TodoList(uuid, title, createdAt, changedAt, position, accessCounter + INCREASE);
    }

    @Override
    public TodoList normalizeAccessCounter() {
        return new TodoList(uuid, title, createdAt, changedAt, position, Double.valueOf(accessCounter / NORMALIZE).longValue());
    }

    @Override
    public long calculateImportance() {
        // Function graph: https://www.google.de/search?q=-%28ln+%28x%2F240%29%29*3&oq=-%28ln+%28x%2F240%29%29*3
        final double creationDifference = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - createdAt.getTime()) + 1;
        final double changeDifference = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - changedAt.getTime());

        final double creationMultiplier = Math.max(1D, -Math.log(creationDifference / 240D) * 3D); // 240D are 10 Days in hours // result is a number between 16.44 and 1
        final double changeMultiplier = (changeDifference < 32D) ? 1.3D : 1D;
        return Double.valueOf(creationMultiplier * changeMultiplier * accessCounter).longValue();
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

    public long getAccessCounter() {
        return accessCounter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TodoList{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", changedAt=").append(changedAt);
        sb.append(", position=").append(position);
        sb.append(", accessCounter=").append(accessCounter);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        TodoList todoList = (TodoList) o;
        return position == todoList.position &&
                accessCounter == todoList.accessCounter &&
                Objects.equals(uuid, todoList.uuid) &&
                Objects.equals(title, todoList.title) &&
                Objects.equals(createdAt, todoList.createdAt) &&
                Objects.equals(changedAt, todoList.changedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, createdAt, changedAt, position, accessCounter);
    }
}
