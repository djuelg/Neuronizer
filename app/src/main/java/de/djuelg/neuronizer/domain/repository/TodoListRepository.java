package de.djuelg.neuronizer.domain.repository;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;

/**
 * A repository that is responsible for the page of a single todolist
 */
public interface TodoListRepository {

    TodoList getTodoListById(String uuid);

    TodoListHeader getHeaderById(String uuid);

    TodoListItem getItemById(String uuid);

    boolean insert(TodoListHeader header);

    boolean insert(TodoListItem item);

    void delete(TodoListHeader deletedItem);

    void delete(TodoListItem deletedItem);

    void update(TodoListHeader updatedItem);

    void update(TodoListItem updatedItem);

}
