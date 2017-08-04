package de.djuelg.neuronizer.domain.repository;

import com.fernandocejas.arrow.optional.Optional;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;

/**
 * A repository that is responsible for the landingpage with previews
 */
public interface PreviewRepository {
    Iterable<TodoListPreview> getPreviews(ItemsPerPreview itemsPerPreview);

    Optional<TodoList> getTodoListById(String uuid);

    boolean insert(TodoList todoList);

    void update(TodoList updatedItem);

    void delete(TodoList deletedItem);

}
