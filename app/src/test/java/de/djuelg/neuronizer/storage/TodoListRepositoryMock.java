package de.djuelg.neuronizer.storage;

import java.util.HashSet;
import java.util.Set;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class TodoListRepositoryMock implements TodoListRepository {

    public int insertCount;
    public Set<String> uuids;

    public TodoListRepositoryMock() {
        insertCount = 0;
        uuids = new HashSet<>();
    }

    @Override
    public Iterable<TodoListPreview> getPreviews() {
        return null;
    }

    @Override
    public boolean insert(TodoList todoList) {
        insertCount++;
        boolean isNewUuid = !uuids.contains(todoList.getUuid());
        uuids.add(todoList.getUuid());
        return isNewUuid;
    }
}
