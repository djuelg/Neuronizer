package de.djuelg.neuronizer.storage;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

/**
 * Created by dmilicic on 1/29/16.
 */
public class PreviewRepositoryImpl implements PreviewRepository {

    private final Realm realm;

    public PreviewRepositoryImpl() {
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public void close() {
        realm.close();
    }

    @Override
    public Iterable<TodoListPreview> getPreviews() {
        RealmResults<TodoListDAO> allTodoListDAO = realm.where(TodoListDAO.class).findAll();
        List<TodoListPreview> previews = new ArrayList<>(allTodoListDAO.size());

        for (TodoListDAO todoListDAO : allTodoListDAO) {
            previews.add(constructPreview(todoListDAO));
        }
        return previews;
    }

    private TodoListPreview constructPreview(TodoListDAO todoListDAO) {
        TodoListHeaderDAO headerDAO = realm.where(TodoListHeaderDAO.class).equalTo("parentTodoListUuid", todoListDAO.getUuid()).findFirst();
        TodoList todoList = RealmConverter.convert(todoListDAO);

        if (headerDAO == null) {
            return new TodoListPreview(todoList);
        } else {
            TodoListHeader header = RealmConverter.convert(headerDAO);
            Iterable<TodoListItem> items = getItemPreviewOfHeader(header);
            return new TodoListPreview(todoList, header, items);
        }
    }

    private Iterable<TodoListItem> getItemPreviewOfHeader(TodoListHeader header) {
        RealmResults<TodoListItemDAO> itemDAOs = realm.where(TodoListItemDAO.class)
                .equalTo("parentTodoListUuid", header.getParentTodoListUuid())
                .equalTo("parentHeaderUuid", header.getUuid())
                .findAll();

        // TODO move "2" to config
        List<TodoListItem> items = new ArrayList<>(2);
        for (TodoListItemDAO itemDAO : itemDAOs.subList(0, 2)) {
            items.add(RealmConverter.convert(itemDAO));
        }
        return items;
    }

    @Override
    public boolean insert(TodoList todoList) {
        final TodoListDAO dao = RealmConverter.convert(todoList);

        realm.beginTransaction();
        try {
            realm.copyToRealm(dao);
            realm.commitTransaction();
        } catch (Throwable throwable) {
            realm.cancelTransaction();
            return false;
        }
        return true;
    }

    @Override
    public TodoList getTodoListById(String uuid) {
        // TODO implement
        return null;
    }

    @Override
    public void update(TodoList updatedItem) {
        // TODO implement
    }

    @Override
    public void delete(TodoList deletedItem) {
        // TODO implement
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!realm.isClosed()) throw new RealmException("close() method must be called before finalization!");
    }
}
