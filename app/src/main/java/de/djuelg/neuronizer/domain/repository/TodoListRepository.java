package de.djuelg.neuronizer.domain.repository;

import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;

/**
 * A repository that is responsible for the page of a single todolist
 */
public interface TodoListRepository {

    List<TodoList> getAll();

    Optional<TodoList> getTodoListById(String uuid);

    Optional<TodoListHeader> getHeaderById(String uuid);

    Optional<TodoListItem> getItemById(String uuid);

    Iterable<TodoListSection> getSectionsOfTodoListId(String uuid);

    Iterable<TodoListHeader> getHeadersOfTodoListId(String uuid);

    int getHeaderCountOfTodoList(String uuid);

    int getSubItemCountOfHeader(String uuid);

    boolean insert(TodoList todoList);

    boolean insert(TodoListHeader header);

    boolean insert(TodoListItem item);

    void delete(TodoList deletedTodoList);

    /**
     * Sub items have to be deleted, too
     */
    void delete(TodoListHeader deletedHeader);

    void delete(TodoListItem deletedItem);

    void update(TodoList updatedTodoList);

    void update(TodoListHeader updatedHeader);

    void update(TodoListItem updatedItem);
}
