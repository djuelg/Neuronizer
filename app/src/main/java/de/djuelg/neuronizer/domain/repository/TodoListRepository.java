package de.djuelg.neuronizer.domain.repository;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListPreview;

/**
 * A repository that is responsible for getting our welcome message.
 */
public interface TodoListRepository {
    Iterable<TodoListPreview> getPreviews();

    boolean insert(TodoList todoList);
}
