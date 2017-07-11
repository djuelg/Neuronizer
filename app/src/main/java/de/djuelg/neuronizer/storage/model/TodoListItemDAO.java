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
    private long deadlineAt;
    private boolean hasDeadline;
    private boolean important;
    private String details;
    @Required
    private String parentTodoListUuid;
    @Required
    private String parentHeaderUuid;

    public TodoListItemDAO() {
    }

    public TodoListItemDAO(String uuid, String title, long createdAt, long changedAt, int position, Long deadlineAt, boolean hasDeadline, boolean important, String details, String parentTodoListUuid, String parentHeaderUuid) {
        this.uuid = uuid;
        this.title = title;
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.position = position;
        if (deadlineAt != null) {
            this.hasDeadline = true;
            this.deadlineAt = deadlineAt;
        } else {
            this.hasDeadline = false;
        }
        this.important = important;
        this.details = details;
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

    public long getDeadlineAt() {
        return deadlineAt;
    }

    public boolean isHasDeadline() {
        return hasDeadline;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChangedAt(long changedAt) {
        this.changedAt = changedAt;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setDeadlineAt(long deadlineAt) {
        this.deadlineAt = deadlineAt;
    }

    public void setHasDeadline(boolean hasDeadline) {
        this.hasDeadline = hasDeadline;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setParentTodoListUuid(String parentTodoListUuid) {
        this.parentTodoListUuid = parentTodoListUuid;
    }

    public void setParentHeaderUuid(String parentHeaderUuid) {
        this.parentHeaderUuid = parentHeaderUuid;
    }
}
