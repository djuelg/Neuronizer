package de.djuelg.neuronizer.storage;

import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.converter.TodoListDAOConverter;
import de.djuelg.neuronizer.storage.converter.TodoListHeaderDAOConverter;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by dmilicic on 1/29/16.
 */
public class PreviewRepositoryImpl implements PreviewRepository {

    private final RealmConfiguration configuration;

    public PreviewRepositoryImpl() {
        this(Realm.getDefaultConfiguration());
    }

    // RealmConfiguration injectable for testing
    PreviewRepositoryImpl(RealmConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Iterable<TodoListPreview> getPreviews(ItemsPerPreview itemsPerPreview) {
        Realm realm = Realm.getInstance(configuration);
        RealmResults<TodoListDAO> allTodoListDAO = realm.where(TodoListDAO.class).findAllSorted("position", Sort.DESCENDING);
        List<TodoListPreview> previews = new ArrayList<>(allTodoListDAO.size());

        for (TodoListDAO todoListDAO : allTodoListDAO) {
            previews.add(constructPreview(realm, todoListDAO, itemsPerPreview));
        }
        realm.close();
        return previews;
    }

    private TodoListPreview constructPreview(Realm realm, TodoListDAO todoListDAO, ItemsPerPreview itemsPerPreview) {
        TodoList todoList = RealmConverter.convert(todoListDAO);

        Optional<TodoListHeaderDAO> headerDAO = Optional.fromNullable(realm.where(TodoListHeaderDAO.class)
                .equalTo("parentTodoListUuid", todoListDAO.getUuid())
                .findAllSorted("position", Sort.DESCENDING).where().findFirst());
        TodoListHeader header = headerDAO.transform(new TodoListHeaderDAOConverter()).orNull();
        List<TodoListItem> items = getItemPreviewOfHeader(realm, header, itemsPerPreview);

        return new TodoListPreview(todoList, header, items);
    }

    private List<TodoListItem> getItemPreviewOfHeader(Realm realm, TodoListHeader header, ItemsPerPreview itemsPerPreview) {
        if (header == null || itemsPerPreview.areZero()) return new ArrayList<>(0);

        RealmResults<TodoListItemDAO> itemDAOs = realm.where(TodoListItemDAO.class)
                .equalTo("parentTodoListUuid", header.getParentTodoListUuid())
                .equalTo("parentHeaderUuid", header.getUuid())
                .findAllSorted("position", Sort.DESCENDING);

        int size = Math.min(itemDAOs.size(), itemsPerPreview.getCount());
        List<TodoListItem> items = new ArrayList<>(size);
        if (size > 0) {
            for (TodoListItemDAO itemDAO : itemDAOs.subList(0, size)) {
                items.add(RealmConverter.convert(itemDAO));
            }
        }
        return items;
    }

    @Override
    public boolean insert(TodoList todoList) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListDAO dao = RealmConverter.convert(todoList);

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
    public Optional<TodoList> getTodoListById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        Optional<TodoListDAO> todoListDAO = Optional.fromNullable(realm.where(TodoListDAO.class).equalTo("uuid", uuid).findFirst());
        Optional<TodoList> todoList = todoListDAO.transform(new TodoListDAOConverter());
        realm.close();
        return todoList;
    }

    @Override
    public int getNumberOfTodoLists() {
        Realm realm = Realm.getInstance(configuration);
        int size = (int) realm.where(TodoListDAO.class).count();
        realm.close();
        return size;
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
    public void delete(final TodoList deletedItem) {
        Realm realm = Realm.getInstance(configuration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListDAO dao = realm.where(TodoListDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
        realm.close();
    }
}
