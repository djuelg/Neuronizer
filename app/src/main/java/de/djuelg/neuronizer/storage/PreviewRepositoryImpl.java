package de.djuelg.neuronizer.storage;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.model.ItemsPerPreview;
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
        this(Realm.getDefaultInstance());
    }

    // Realm injectable for testing
    PreviewRepositoryImpl(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void close() {
        realm.close();
    }

    @Override
    public Iterable<TodoListPreview> getPreviews(ItemsPerPreview itemsPerPreview) {
        RealmResults<TodoListDAO> allTodoListDAO = realm.where(TodoListDAO.class).findAll();
        List<TodoListPreview> previews = new ArrayList<>(allTodoListDAO.size());

        for (TodoListDAO todoListDAO : allTodoListDAO) {
            previews.add(constructPreview(todoListDAO, itemsPerPreview));
        }
        return previews;
    }

    private TodoListPreview constructPreview(TodoListDAO todoListDAO, ItemsPerPreview itemsPerPreview) {
        TodoListHeaderDAO headerDAO = realm.where(TodoListHeaderDAO.class).equalTo("parentTodoListUuid", todoListDAO.getUuid()).findFirst();
        TodoList todoList = RealmConverter.convert(todoListDAO);

        if (headerDAO == null) {
            return new TodoListPreview(todoList);
        } else {
            TodoListHeader header = RealmConverter.convert(headerDAO);
            Iterable<TodoListItem> items = getItemPreviewOfHeader(header, itemsPerPreview);
            return new TodoListPreview(todoList, header, items);
        }
    }

    private Iterable<TodoListItem> getItemPreviewOfHeader(TodoListHeader header, ItemsPerPreview itemsPerPreview) {
        if (itemsPerPreview.areZero()) return new ArrayList<>(0);

        RealmResults<TodoListItemDAO> itemDAOs = realm.where(TodoListItemDAO.class)
                .equalTo("parentTodoListUuid", header.getParentTodoListUuid())
                .equalTo("parentHeaderUuid", header.getUuid())
                .findAll();

        int size = Math.min(itemDAOs.size(), itemsPerPreview.getCount());
        List<TodoListItem> items = new ArrayList<>(size);
        for (TodoListItemDAO itemDAO : itemDAOs.subList(0, size-1)) {
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
        TodoListDAO todoListDAO = realm.where(TodoListDAO.class).equalTo("uuid", uuid).findFirst();
        return (todoListDAO != null)
                ? RealmConverter.convert(todoListDAO)
                : null;
    }

    @Override
    public void update(TodoList updatedItem) {
        final TodoListDAO todoListDAO = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(todoListDAO);
            }
        });
    }

    @Override
    public void delete(final TodoList deletedItem) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListDAO dao = realm.where(TodoListDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        if (!realm.isClosed()) throw new RealmException("close() method must be called before finalization!");
        super.finalize();
    }
}
