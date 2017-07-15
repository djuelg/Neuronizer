package de.djuelg.neuronizer.storage;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import de.djuelg.neuronizer.domain.model.Color;
import de.djuelg.neuronizer.domain.model.Deadline;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import io.realm.exceptions.RealmException;

/**
 * Created by djuelg on 10.07.17.
 */

public class TodoListRepositoryMock implements TodoListRepository {

    public int insertCount;
    public int updateCount;
    public int deleteCount;
    public boolean closedCalled;
    public Set<String> uuids;
    private TodoList alwaysSameTodoList;
    private TodoListHeader alwaysSameHeader;
    private TodoListItem alwaysSameItem;

    public TodoListRepositoryMock() {
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
        closedCalled = false;
        uuids = new HashSet<>();
        alwaysSameTodoList = new TodoList("In-Database", 42);
        alwaysSameHeader = new TodoListHeader("Header", 0, new Color(0), "uuid0");
        alwaysSameItem = new TodoListItem("Item", 0, new Deadline(), false, "", "uuid0", alwaysSameHeader.getUuid());
    }

    @Override
    public TodoList getTodoListById(String uuid) {
        if ("MISSING_UUID".equals(uuid)) {
            return null;
        }
        return alwaysSameTodoList;
    }

    @Override
    public TodoListHeader getHeaderById(String uuid) {
        if ("MISSING_UUID".equals(uuid)) {
            return null;
        }
        return alwaysSameHeader;
    }

    @Override
    public TodoListItem getItemById(String uuid) {
        if ("MISSING_UUID".equals(uuid)) {
            return null;
        }
        return alwaysSameItem;
    }

    @Override
    public boolean insert(TodoListHeader header) {
        // a little bit of java reflection to check for duplicated uuid
        if ("HEADER_DUPLICATION".equals(header.getTitle()) && insertCount <= 1) {
            try {
                Field uuidField = header.getClass().getDeclaredField("uuid");
                uuidField.setAccessible(true);
                uuidField.set(header, "DUPLICATED_UUID");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        insertCount++;
        boolean isNewUuid = !uuids.contains(header.getUuid());
        uuids.add(header.getUuid());
        return isNewUuid;
    }

    @Override
    public boolean insert(TodoListItem item) {
        // a little bit of java reflection to check for duplicated uuid
        if ("ITEM_DUPLICATION".equals(item.getTitle()) && insertCount <= 1) {
            try {
                Field uuidField = item.getClass().getDeclaredField("uuid");
                uuidField.setAccessible(true);
                uuidField.set(item, "DUPLICATED_UUID");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        insertCount++;
        boolean isNewUuid = !uuids.contains(item.getUuid());
        uuids.add(item.getUuid());
        return isNewUuid;
    }

    @Override
    public void delete(TodoListHeader deletedItem) {
        deleteCount++;
    }

    @Override
    public void delete(TodoListItem deletedItem) {
        deleteCount++;
    }

    @Override
    public void update(TodoListHeader updatedItem) {
        updateCount++;
        uuids.add(updatedItem.getUuid());
    }

    @Override
    public void update(TodoListItem updatedItem) {
        updateCount++;
        uuids.add(updatedItem.getUuid());
    }

    @Override
    public void close() {
        closedCalled = true;
    }

    // reminder to call close() even in tests
    @Override
    protected void finalize() throws Throwable {
        if (!closedCalled) throw new RealmException("close() method must be called before finalization!");
        super.finalize();
    }
}
