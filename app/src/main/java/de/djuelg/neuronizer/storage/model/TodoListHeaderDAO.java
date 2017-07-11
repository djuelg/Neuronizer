package de.djuelg.neuronizer.storage.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Domi on 03.09.2016.
 */
public class TodoListHeaderDAO extends RealmObject {
    @PrimaryKey
    private String uuid;
    @Required
    private String title;
    private long createdAt;
    private long changedAt;
    private int position;
    private int color;
    @Required
    private String parentTodoListUuid;

    public TodoListHeaderDAO() {
    }

    public TodoListHeaderDAO(String uuid, String title, long createdAt, long changedAt, int position, int color, String parentTodoListUuid) {
        this.uuid = uuid;
        this.title = title;
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.position = position;
        this.color = color;
        this.parentTodoListUuid = parentTodoListUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getChangedAt() {
        return changedAt;
    }

    public int getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }

    public String getParentTodoListUuid() {
        return parentTodoListUuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChangedAt(long changedAt) {
        this.changedAt = changedAt;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setParentTodoListUuid(String parentTodoListUuid) {
        this.parentTodoListUuid = parentTodoListUuid;
    }
}
