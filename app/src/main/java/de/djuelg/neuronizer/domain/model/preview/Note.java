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

public class Note implements BaseModel {

    private static final int INCREASE = 1;
    private static final int NORMALIZE = 2;

    private final String uuid;
    private final String title;
    private final Date createdAt;
    private final Date changedAt;
    private final int position;
    private final long accessCounter;
    private final String body;

    // constructor for first time creation
    public Note(String title, int position) {
        this(UUID.randomUUID().toString(), title, new Date(), new Date(), position, 0L, "");
    }

    // constructor for model updates / read from database
    public Note(String uuid, String title, Date createdAt, Date changedAt, int position, long accessCounter, String body) {
        this.uuid = Objects.requireNonNull(uuid);
        this.title = Objects.requireNonNull(title);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.changedAt = Objects.requireNonNull(changedAt);
        this.position = Objects.requireNonNull(position);
        this.accessCounter = Objects.requireNonNull(accessCounter);
        this.body = Objects.requireNonNull(body);
    }

    public Note update(String title, int position) {
        if (this.equals(new Note(uuid, title, createdAt, changedAt, position, accessCounter, body))) return this;
        return new Note(uuid, title, createdAt, changedAt, position, accessCounter, body);
    }

    public Note updateLastChange() {
        return new Note(uuid, title, createdAt, new Date(), position, accessCounter, body);
    }

    public Note increaseAccessCounter() {
        return new Note(uuid, title, createdAt, changedAt, position, accessCounter + INCREASE, body);
    }

    public Note normalizeAccessCounter() {
        return new Note(uuid, title, createdAt, changedAt, position, Double.valueOf(accessCounter / NORMALIZE).longValue(), body);
    }

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

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Note{" +
                "uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", changedAt=" + changedAt +
                ", position=" + position +
                ", accessCounter=" + accessCounter +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Note note = (Note) o;
        return position == note.position &&
                accessCounter == note.accessCounter &&
                Objects.equals(uuid, note.uuid) &&
                Objects.equals(title, note.title) &&
                Objects.equals(createdAt, note.createdAt) &&
                Objects.equals(changedAt, note.changedAt) &&
                Objects.equals(body, note.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, title, createdAt, changedAt, position, accessCounter, body);
    }
}
