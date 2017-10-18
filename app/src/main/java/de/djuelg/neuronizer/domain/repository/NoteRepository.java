package de.djuelg.neuronizer.domain.repository;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.model.preview.Note;

/**
 * A repository that is responsible for the single note pages
 */
public interface NoteRepository {

    Optional<Note> get(String uuid);

    boolean insert(Note note);

    void update(Note updatedNote);

    void delete(Note deletedNote);
}
