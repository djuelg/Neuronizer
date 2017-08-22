package de.djuelg.neuronizer.storage.model;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Domi on 03.09.2016.
 */
public class NoteDAO extends RealmObject {

    @Required
    @Index
    @PrimaryKey
    private String uuid;
    @Required
    private String title;
    private long createdAt;
    private long changedAt;
    private int position;
    private long accessCounter;
    @Required
    private String body;

    public NoteDAO() {
    }

    public NoteDAO(String uuid, String title, long createdAt, long changedAt, int position, long accessCounter, String body) {
        this.uuid = uuid;
        this.title = title;
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.position = position;
        this.accessCounter = accessCounter;
        this.body = body;
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

    public String getBody() {
        return body;
    }

    public long getAccessCounter() {
        return accessCounter;
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

    public void setBody(String body) {
        this.body = body;
    }

    public void setAccessCounter(long accessCounter) {
        this.accessCounter = accessCounter;
    }
}
