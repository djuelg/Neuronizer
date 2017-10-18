package de.djuelg.neuronizer.domain.model.preview;

import java.util.Objects;

import de.djuelg.neuronizer.domain.model.BaseModel;

import static de.djuelg.neuronizer.presentation.ui.custom.HtmlStripper.stripHtml;

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
    public BaseModel getBaseItem() {
        return note;
    }

    @Override
    public String getSubtitle() {
        return "";
    }

    @Override
    public String getDetails() {
        String body = stripHtml(note.getBody());
        return (body.length() < 32)
                ? body
                : body.substring(0, 32) + "...";
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
