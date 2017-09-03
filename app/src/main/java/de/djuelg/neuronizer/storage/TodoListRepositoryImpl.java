package de.djuelg.neuronizer.storage;

import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.model.todolist.TodoListSection;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.converter.TodoListDAOConverter;
import de.djuelg.neuronizer.storage.converter.TodoListHeaderDAOConverter;
import de.djuelg.neuronizer.storage.converter.TodoListItemDAOConverter;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by dmilicic on 1/29/16.
 */
public class TodoListRepositoryImpl implements TodoListRepository {

    private final RealmConfiguration configuration;

    public TodoListRepositoryImpl() {
        this(Realm.getDefaultConfiguration());
    }

    // RealmConfiguration injectable for testing
    TodoListRepositoryImpl(RealmConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<TodoList> getTodoListById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        Optional<TodoListDAO> todoListDAO = Optional.fromNullable(realm.where(TodoListDAO.class).equalTo("uuid", uuid).findFirst());
        Optional<TodoList> todoList = todoListDAO.transform(new TodoListDAOConverter());
        realm.close();
        return todoList;
    }

    @Override
    public Optional<TodoListHeader> getHeaderById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        Optional<TodoListHeaderDAO> headerDAO = Optional.fromNullable(realm.where(TodoListHeaderDAO.class).equalTo("uuid", uuid).findFirst());
        Optional<TodoListHeader> header = headerDAO.transform(new TodoListHeaderDAOConverter());
        realm.close();
        return header;
    }

    @Override
    public Optional<TodoListItem> getItemById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        Optional<TodoListItemDAO> itemDAO = Optional.fromNullable(realm.where(TodoListItemDAO.class).equalTo("uuid", uuid).findFirst());
        Optional<TodoListItem> item = itemDAO.transform(new TodoListItemDAOConverter());
        realm.close();
        return item;
    }

    @Override
    public List<TodoList> getTodoLists() {
        Realm realm = Realm.getInstance(configuration);
        RealmResults<TodoListDAO> todoListDAOs = realm.where(TodoListDAO.class).findAll();
        List<TodoList> todoLists = new ArrayList<>(todoListDAOs.size());
        for (TodoListDAO dao : todoListDAOs) {
            todoLists.add(RealmConverter.convert(dao));
        }
        realm.close();
        return todoLists;
    }

    @Override
    public List<TodoListSection> getSectionsOfTodoListId(String todoListUuid) {
        Realm realm = Realm.getInstance(configuration);
        RealmResults<TodoListHeaderDAO> headerDAOs = realm.where(TodoListHeaderDAO.class).equalTo("parentTodoListUuid", todoListUuid).findAll();
        List<TodoListSection> sections = new ArrayList<>(headerDAOs.size());
        for (TodoListHeaderDAO dao : headerDAOs) {
            sections.add(constructSection(realm, dao));
        }
        realm.close();
        return sections;
    }

    @Override
    public List<TodoListHeader> getHeadersOfTodoListId(String todoListUuid) {
        Realm realm = Realm.getInstance(configuration);
        RealmResults<TodoListHeaderDAO> headerDAOs = realm.where(TodoListHeaderDAO.class).equalTo("parentTodoListUuid", todoListUuid).findAll();
        List<TodoListHeader> headers = new ArrayList<>(headerDAOs.size());
        for (TodoListHeaderDAO dao : headerDAOs) {
            headers.add(RealmConverter.convert(dao));
        }
        realm.close();
        return headers;
    }

    @Override
    public int getNumberOfHeaders(String todoListUuid) {
        Realm realm = Realm.getInstance(configuration);
        int size = (int) realm.where(TodoListHeaderDAO.class).equalTo("parentTodoListUuid", todoListUuid).count();
        realm.close();
        return size;
    }

    @Override
    public int getNumberOfSubItems(String headerUuid) {
        Realm realm = Realm.getInstance(configuration);
        int size = (int) realm.where(TodoListItemDAO.class).equalTo("parentHeaderUuid", headerUuid).count();
        realm.close();
        return size;
    }

    private TodoListSection constructSection(Realm realm, TodoListHeaderDAO headerDAO) {
        RealmResults<TodoListItemDAO> itemDAOs = realm.where(TodoListItemDAO.class)
                .equalTo("parentTodoListUuid", headerDAO.getParentTodoListUuid())
                .equalTo("parentHeaderUuid", headerDAO.getUuid()).findAll();
        List<TodoListItem> items = new ArrayList<>(itemDAOs.size());

        TodoListHeader header = RealmConverter.convert(headerDAO);
        for (TodoListItemDAO dao : itemDAOs) {
            items.add(RealmConverter.convert(dao));
        }
        return new TodoListSection(header, items);
    }

    @Override
    public boolean insert(TodoListHeader header) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListHeaderDAO dao = RealmConverter.convert(header);

        realm.beginTransaction();
        try {
            realm.copyToRealm(dao);
            realm.commitTransaction();
        } catch (Throwable throwable) {
            realm.cancelTransaction();
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }

    @Override
    public boolean insert(TodoListItem item) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListItemDAO dao = RealmConverter.convert(item);

        realm.beginTransaction();
        try {
            realm.copyToRealm(dao);
            realm.commitTransaction();
        } catch (Throwable throwable) {
            realm.cancelTransaction();
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }

    @Override
    public void delete(final TodoListHeader deletedItem) {
        Realm realm = Realm.getInstance(configuration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(TodoListItemDAO.class).equalTo("parentHeaderUuid", deletedItem.getUuid()).findAll().deleteAllFromRealm();
                TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public void delete(final TodoListItem deletedItem) {
        Realm realm = Realm.getInstance(configuration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public void update(TodoList updatedItem) {
        Realm realm = Realm.getInstance(configuration);

        final TodoListDAO todoListDAO = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(todoListDAO);
            }
        });
        realm.close();
    }

    @Override
    public void update(TodoListHeader updatedItem) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListHeaderDAO dao = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dao);
            }
        });
        realm.close();
    }

    @Override
    public void update(TodoListItem updatedItem) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListItemDAO dao = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dao);
            }
        });
        realm.close();
    }
}
