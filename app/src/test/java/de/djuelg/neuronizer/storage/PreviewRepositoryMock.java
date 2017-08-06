package de.djuelg.neuronizer.storage;

import com.fernandocejas.arrow.optional.Optional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class PreviewRepositoryMock implements PreviewRepository {

    public int insertCount;
    public int updateCount;
    public int deleteCount;
    public Set<String> uuids;
    private TodoList alwaysSameItem;

    public PreviewRepositoryMock() {
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
        uuids = new HashSet<>();
        alwaysSameItem = new TodoList("In-Database", 0);
    }

    @Override
    public Iterable<TodoListPreview> getPreviews(ItemsPerPreview itemsPerPreview) {
        return new ArrayList<>(0);
    }

    @Override
    public Optional<TodoList> getTodoListById(String uuid) {
        return Optional.of(alwaysSameItem);
    }

    @Override
    // TODO Test
    public int getNumberOfTodoLists() {
        return 42;
    }

    @Override
    public boolean insert(TodoList todoList) {
        // a little bit of java reflection to check for duplicated uuid
        if ("TODO_LIST_DUPLICATION".equals(todoList.getTitle()) && insertCount <= 1) {
            try {
                Field uuidField = todoList.getClass().getDeclaredField("uuid");
                uuidField.setAccessible(true);
                uuidField.set(todoList, "DUPLICATED_UUID");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

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
}
