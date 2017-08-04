package de.djuelg.neuronizer.storage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class TodoListRepositoryMock implements TodoListRepository {

    public int insertCount;
    public int updateCount;
    public int deleteCount;
    public Set<String> uuids;
    private TodoList alwaysSameTodoList;
    private TodoListHeader alwaysSameHeader;
    private TodoListItem alwaysSameItem;

    public TodoListRepositoryMock() {
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
        uuids = new HashSet<>();
        alwaysSameTodoList = new TodoList("In-Database");
        alwaysSameHeader = new TodoListHeader("Header", 0, "uuid0");
        alwaysSameItem = new TodoListItem("Item", 0, false, "", "uuid0", alwaysSameHeader.getUuid());
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
    // TODO Test
    // - DisplayTodoListInteractor
    // - new Methods in TodoListRepository
    // - are presenters tested? if yes then test DisplayTodoListPresenter
    public List<TodoListSection> getSectionsOfTodoListId(String todoListUuid) {
        List<TodoListSection> sections = new ArrayList<>(1);
        List<TodoListItem> items = new ArrayList<TodoListItem>(1);
        items.add(alwaysSameItem);
        sections.add(new TodoListSection(alwaysSameHeader, items));
        return sections;
    }

    @Override
    // TODO Test
    // - DisplayHeadersInteractor
    // - new Methods in TodoListRepository
    // - are presenters tested? if yes then test DisplayHeadersPresenter
    public List<TodoListHeader> getHeadersOfTodoListId(String todoListUuid) {
        List<TodoListHeader> headers = new ArrayList<>(1);
        headers.add(alwaysSameHeader);
        return headers;
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

}
