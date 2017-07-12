package de.djuelg.neuronizer.storage;

import java.util.HashSet;
import java.util.Set;

import de.djuelg.neuronizer.domain.model.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import io.realm.exceptions.RealmException;

/**
 * Created by djuelg on 10.07.17.
 */

public class PreviewRepositoryMock implements PreviewRepository {

    public int insertCount;
    public int updateCount;
    public int deleteCount;
    public boolean closedCalled;
    public Set<String> uuids;
    private TodoList alwaysSameItem;

    public PreviewRepositoryMock() {
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
        closedCalled = false;
        uuids = new HashSet<>();
        alwaysSameItem = new TodoList("In-Database", 42);
    }

    @Override
    public void close() {
        closedCalled = true;
    }

    @Override
    public Iterable<TodoListPreview> getPreviews(ItemsPerPreview itemsPerPreview) {
        return null;
    }

    @Override
    public TodoList getTodoListById(String uuid) {
        return alwaysSameItem;
    }

    @Override
    public boolean insert(TodoList todoList) {
        insertCount++;
        boolean isNewUuid = !uuids.contains(todoList.getUuid());
        uuids.add(todoList.getUuid());
        return isNewUuid;
    }

    @Override
    public void update(TodoList updatedItem) {
        updateCount++;
        uuids.add(updatedItem.getUuid());
    }

    @Override
    public void delete(TodoList deletedItem) {
        deleteCount++;
    }

    // reminder to call close() even in tests
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!closedCalled) throw new RealmException("close() method must be called before finalization!");
    }
}
