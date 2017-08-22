package de.djuelg.neuronizer.storage;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.fernandocejas.arrow.optional.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static de.djuelg.neuronizer.AndroidApplication.SCHEMA_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Domi on 13.07.2017.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class PreviewRepositoryImplTest {

    public final static RealmConfiguration TEST_REALM_CONFIG = new RealmConfiguration.Builder()
            .name("TEST_REALM_CONFIG")
            .schemaVersion(SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
            .inMemory()
            .build();

    private Realm realm;
    private PreviewRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        realm = Realm.getInstance(TEST_REALM_CONFIG);
        repository = new PreviewRepositoryImpl(TEST_REALM_CONFIG);

        fillRealm();
    }

    private void fillRealm() {
        final TodoListDAO todoListDAO = new TodoListDAO("uuid0", "Todo List 1", 0, 0, 0);
        final TodoListHeaderDAO headerDAO = new TodoListHeaderDAO("uuid1", "Header 1", 0, 0, 0, false, "uuid0");
        final TodoListItemDAO itemDAO = new TodoListItemDAO("uuid2", "Item 1", 0, 0, 0, false, "", false, "uuid0", "uuid1");

        final TodoListHeaderDAO headerDAO2 = new TodoListHeaderDAO("uuid3", "Header 2", 0, 0, 0, false, "uuid3");
        final TodoListItemDAO itemDAO2 = new TodoListItemDAO("uuid4", "Item 2", 0, 0, 0, false, "", false, "uuid0", "uuid3");
        final TodoListItemDAO itemDAO3 = new TodoListItemDAO("uuid5", "Item 3", 0, 0, 0, false, "", false, "uuid0", "uuid3");

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(todoListDAO);
                realm.copyToRealm(headerDAO);
                realm.copyToRealm(headerDAO2);
                realm.copyToRealm(itemDAO);
                realm.copyToRealm(itemDAO2);
                realm.copyToRealm(itemDAO3);
            }
        });
    }

    private void clearRealm() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    private TodoList createTodoList() {
        return new TodoList("InsertTest", 0);
    }

    @After
    public void tearDown() throws Exception {
        clearRealm();
        realm.close();
    }

    @Test
    public void testEmptyGetPreview() {
        clearRealm();
        Iterable<TodoListPreview> previews = repository.getPreviews(new ItemsPerPreview(0));
        assertEquals(false, previews.iterator().hasNext());
        fillRealm();
    }

    @Test
    public void testGetPreviewCorrectHeader() {
        Iterable<TodoListPreview> previews = repository.getPreviews(new ItemsPerPreview(0));
        assertEquals("uuid1", previews.iterator().next().getHeader().get().getUuid());
    }

    @Test
    public void testGetPreviewManyItemsPerPreview() {
        List<TodoListPreview> previews = (List<TodoListPreview>) repository.getPreviews(new ItemsPerPreview(100));
        assertEquals("uuid1", previews.iterator().next().getHeader().get().getUuid());
        assertEquals(1, previews.size());
    }

    @Test
    public void testItemInsert() {
        TodoList todoList = createTodoList();
        boolean success = repository.insert(todoList);
        assertTrue(success);
        TodoListDAO dao = realm.where(TodoListDAO.class).equalTo("uuid", todoList.getUuid()).findFirst();
        assertNotNull(dao);
    }

    @Test
    public void testItemInsertTwice() {
        clearRealm();
        TodoList todoList = createTodoList();
        repository.insert(todoList);
        boolean success = repository.insert(todoList);
        assertFalse(success);
        TodoListDAO dao = realm.where(TodoListDAO.class).equalTo("uuid", todoList.getUuid()).findFirst();
        assertNotNull(dao);
        fillRealm();
    }

    @Test
    public void testGetTodoListById() {
        TodoList todoList = createTodoList();
        repository.insert(todoList);
        Optional<TodoList> fromDb = repository.getTodoListById(todoList.getUuid());
        assertEquals(todoList, fromDb.get());
    }

    @Test
    public void testGetTodoListByIdIsNull() {
        TodoList todoList = createTodoList();
        repository.insert(todoList);
        Optional<TodoList> fromDb = repository.getTodoListById("NOT_EXISTING_UUID");
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testUpdate() throws InterruptedException {
        TodoList todoList = createTodoList();
        repository.insert(todoList);
        Thread.sleep(200);
        repository.update(todoList.update("New Title", 0));
        Optional<TodoList> fromDb = repository.getTodoListById(todoList.getUuid());
        assertEquals(todoList.getCreatedAt(), fromDb.get().getCreatedAt());
        assertNotEquals(todoList.getChangedAt(), fromDb.get().getChangedAt());
        assertNotEquals(todoList.getTitle(), fromDb.get().getTitle());
    }

    @Test
    public void testUpdateAsInsert() {
        TodoList todoList = createTodoList();
        repository.update(todoList);
        Optional<TodoList> fromDb = repository.getTodoListById(todoList.getUuid());
        assertEquals(todoList, fromDb.get());
    }

    @Test
    public void testDelete() {
        TodoList todoList = createTodoList();
        repository.insert(todoList);
        repository.delete(todoList);

        Optional<TodoList> fromDb = repository.getTodoListById(todoList.getUuid());
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testDeleteNotExisting() {
        TodoList todoList = createTodoList();
        repository.delete(todoList);

        Optional<TodoList> fromDb = repository.getTodoListById(todoList.getUuid());
        assertFalse(fromDb.isPresent());
    }
}