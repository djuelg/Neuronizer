package de.djuelg.neuronizer.storage;

import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.NotePreview;
import de.djuelg.neuronizer.domain.model.preview.Preview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.converter.TodoListHeaderDAOConverter;
import de.djuelg.neuronizer.storage.model.NoteDAO;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

import static de.djuelg.neuronizer.storage.RepositoryManager.createConfiguration;

/**
 * Created by dmilicic on 1/29/16.
 */
public class PreviewRepositoryImpl implements PreviewRepository {

    private final RealmConfiguration configuration;

    public PreviewRepositoryImpl(String realmName) {
        this.configuration = createConfiguration(realmName);
    }

    // RealmConfiguration injectable for testing
    PreviewRepositoryImpl(RealmConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Iterable<Preview> getAll(ItemsPerPreview itemsPerPreview) {
        Realm realm = Realm.getInstance(configuration);
        RealmResults<TodoListDAO> allTodoListDAO = realm.where(TodoListDAO.class).findAllSorted("position", Sort.DESCENDING);
        RealmResults<NoteDAO> allNoteDAO = realm.where(NoteDAO.class).findAllSorted("position", Sort.DESCENDING);

        List<Preview> previews = new ArrayList<>(allTodoListDAO.size() + allNoteDAO.size());

        for (TodoListDAO todoListDAO : allTodoListDAO) {
            previews.add(constructPreview(realm, todoListDAO, itemsPerPreview));
        }
        for (NoteDAO noteDAO : allNoteDAO) {
            previews.add(new NotePreview(RealmConverter.convert(noteDAO)));
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
    public int count() {
        Realm realm = Realm.getInstance(configuration);
        int size = (int) (realm.where(TodoListDAO.class).count() + realm.where(NoteDAO.class).count());
        realm.close();
        return size;
    }
}
