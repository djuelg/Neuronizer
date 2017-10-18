package de.djuelg.neuronizer.domain.repository;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.model.preview.Note;

/**
 * A repository that is responsible for the single note pages
 */
public interface NoteRepository {

    Optional<Note> getNoteById(String uuid);

    void update(Note updatedNote);
}
