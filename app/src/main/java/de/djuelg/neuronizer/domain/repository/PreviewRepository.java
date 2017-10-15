package de.djuelg.neuronizer.domain.repository;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.model.preview.Preview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;

/**
 * A repository that is responsible for the landingpage with previews
 */
public interface PreviewRepository {
    Iterable<Preview> getPreviews(ItemsPerPreview itemsPerPreview);

    Optional<TodoList> getTodoListById(String uuid);

    Optional<Note> getNoteById(String uuid);

    int getNumberOfPreviews();

    boolean insert(TodoList todoList);

    boolean insert(Note note);

    void update(TodoList updatedItem);

    void update(Note updatedNote);

    /**
     * Corresponding headers and items have to be deleted, too
     */
    void delete(TodoList deletedItem);

    void delete(Note deletedNote);

}
