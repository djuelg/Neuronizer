package de.djuelg.neuronizer.domain.repository;

import de.djuelg.neuronizer.domain.model.TodoList;

/**
 * A repository that is responsible for getting our welcome message.
 */
public interface TodoListRepository {
    Iterable<TodoList> getAllLists();
}
