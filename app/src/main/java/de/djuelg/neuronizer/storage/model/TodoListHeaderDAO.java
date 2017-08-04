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
    private boolean expanded;
    @Required
    private String parentTodoListUuid;

    public TodoListHeaderDAO() {
    }

    public TodoListHeaderDAO(String uuid, String title, long createdAt, long changedAt, int position, boolean expanded, String parentTodoListUuid) {
        this.uuid = uuid;
        this.title = title;
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.position = position;
        this.expanded = expanded;
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

    public boolean isExpanded() {
        return expanded;
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

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public void setParentTodoListUuid(String parentTodoListUuid) {
        this.parentTodoListUuid = parentTodoListUuid;
    }
}
