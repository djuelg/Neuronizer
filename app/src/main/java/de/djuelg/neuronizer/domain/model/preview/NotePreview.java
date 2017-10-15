package de.djuelg.neuronizer.domain.model.preview;

import java.util.Objects;

import de.djuelg.neuronizer.domain.model.BaseModel;

/**
 * Created by djuelg on 09.07.17.
 *
 * Group of TodoList items to display a preview
 */

public class NotePreview implements Preview {

    private final Note note;

    public NotePreview(Note note) {
        this.note = note;
    }

    @Override
    public BaseModel getPreview() {
        return note;
    }

    @Override
    public String getSubtitle() {
        return "";
    }

    @Override
    public String getDetails() {
        return (note.getBody().length() < 32)
                ? note.getBody()
                : note.getBody().substring(32);
    }

    @Override
    public long calculateImportance() {
        return note.calculateImportance();
    }

    @Override
    public int getPosition() {
        return note.getPosition();
    }

    @Override
    public String toString() {
        return "NotePreview{" +
                "note=" + note +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NotePreview that = (NotePreview) o;
        return Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(note);
    }
}
