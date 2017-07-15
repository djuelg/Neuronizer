package de.djuelg.neuronizer.storage;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.exceptions.RealmException;

/**
 * Created by dmilicic on 1/29/16.
 */
public class TodoListRepositoryImpl implements TodoListRepository {

    private final Realm realm;

    public TodoListRepositoryImpl() {
        this(Realm.getDefaultInstance());
    }

    // Realm injectable for testing
    TodoListRepositoryImpl(Realm realm) {
        this.realm = realm;
    }

    @Override
    public void close() {
        realm.close();
    }


    @Override
    public TodoList getTodoListById(String uuid) {
        TodoListDAO todoListDAO = realm.where(TodoListDAO.class).equalTo("uuid", uuid).findFirst();
        return (todoListDAO != null)
                ? RealmConverter.convert(todoListDAO)
                : null;
    }

    @Override
    public TodoListHeader getHeaderById(String uuid) {
        TodoListHeaderDAO headerDAO = realm.where(TodoListHeaderDAO.class).equalTo("uuid", uuid).findFirst();
        return (headerDAO != null)
                ? RealmConverter.convert(headerDAO)
                : null;
    }

    @Override
    public TodoListItem getItemById(String uuid) {
        TodoListItemDAO itemDAO = realm.where(TodoListItemDAO.class).equalTo("uuid", uuid).findFirst();
        return (itemDAO != null)
                ? RealmConverter.convert(itemDAO)
                : null;
    }

    @Override
    public boolean insert(TodoListHeader header) {
        final TodoListHeaderDAO dao = RealmConverter.convert(header);

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
    public boolean insert(TodoListItem item) {
        final TodoListItemDAO dao = RealmConverter.convert(item);

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
    public void delete(final TodoListHeader deletedItem) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
    }

    @Override
    public void delete(final TodoListItem deletedItem) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
    }

    @Override
    public void update(TodoListHeader updatedItem) {
        final TodoListHeaderDAO dao = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dao);
            }
        });
    }

    @Override
    public void update(TodoListItem updatedItem) {
        final TodoListItemDAO dao = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dao);
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        if (!realm.isClosed()) throw new RealmException("close() method must be called before finalization!");
        super.finalize();
    }
}
