package de.djuelg.neuronizer.domain.repository;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;

/**
 * A repository that is responsible for the landingpage with previews
 */
public interface PreviewRepository {
    Iterable<TodoListPreview> getPreviews(ItemsPerPreview itemsPerPreview);

    TodoList getTodoListById(String uuid);

    boolean insert(TodoList todoList);

    void update(TodoList updatedItem);

    void delete(TodoList deletedItem);

}
