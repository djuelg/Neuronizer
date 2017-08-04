package de.djuelg.neuronizer.storage.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Domi on 03.09.2016.
 */
public class TodoListItemDAO extends RealmObject {

    @PrimaryKey
    private String uuid;
    @Required
    private String title;
    private long createdAt;
    private long changedAt;
    private int position;
    private boolean important;
    private String details;
    private boolean done;
    @Required
    private String parentTodoListUuid;
    @Required
    private String parentHeaderUuid;

    public TodoListItemDAO() {
    }

    public TodoListItemDAO(String uuid, String title, long createdAt, long changedAt, int position, boolean important, String details, boolean done, String parentTodoListUuid, String parentHeaderUuid) {
        this.uuid = uuid;
        this.title = title;
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.position = position;
        this.important = important;
        this.details = details;
        this.done = done;
        this.parentTodoListUuid = parentTodoListUuid;
        this.parentHeaderUuid = parentHeaderUuid;
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

    public boolean isImportant() {
        return important;
    }

    public String getDetails() {
        return details;
    }

    public boolean isDone() {
        return done;
    }

    public String getParentTodoListUuid() {
        return parentTodoListUuid;
    }

    public String getParentHeaderUuid() {
        return parentHeaderUuid;
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

    public void setImportant(boolean important) {
        this.important = important;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setParentTodoListUuid(String parentTodoListUuid) {
        this.parentTodoListUuid = parentTodoListUuid;
    }

    public void setParentHeaderUuid(String parentHeaderUuid) {
        this.parentHeaderUuid = parentHeaderUuid;
    }
}
