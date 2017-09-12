package de.djuelg.neuronizer.storage;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.fernandocejas.arrow.optional.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static de.djuelg.neuronizer.storage.RepositoryManager.SCHEMA_VERSION;
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
public class TodoListRepositoryImplTest {

    public final static RealmConfiguration TEST_REALM_CONFIG = new RealmConfiguration.Builder()
            .name("TEST_REALM_CONFIG")
            .schemaVersion(SCHEMA_VERSION)
            .deleteRealmIfMigrationNeeded()
            .inMemory()
            .build();

    private Realm realm;
    private TodoListRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        realm = Realm.getInstance(TEST_REALM_CONFIG);
        repository = new TodoListRepositoryImpl(TEST_REALM_CONFIG);

        fillRealm();
    }

    private void fillRealm() {
        final TodoListDAO todoListDAO = new TodoListDAO("uuid0", "Todo List 1", 0, 0, 0, 0);
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

    private TodoListItem createItem() {
        return new TodoListItem("InsertTest", 0, false, "", "uuid0" ,"uuid1");
    }

    private TodoListHeader createHeader() {
        return new TodoListHeader("InsertTest", 0, "uuid0");
    }

    @After
    public void tearDown() throws Exception {
        clearRealm();
        realm.close();
    }

    @Test
    public void testGetTodoListById() {
        Optional<TodoList> fromDb = repository.getTodoListById("uuid0");
        assertTrue(fromDb.isPresent());
    }

    @Test
    public void testGetTodoListByIdIsNull() {
        Optional<TodoList> fromDb = repository.getTodoListById("NOT_EXISTING_UUID");
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testGetTodoListHeaderById() {
        Optional<TodoListHeader> fromDb = repository.getHeaderById("uuid1");
        assertTrue(fromDb.isPresent());
    }

    @Test
    public void testGetTodoListHeaderByIdIsNull() {
        Optional<TodoListHeader> fromDb = repository.getHeaderById("NOT_EXISTING_UUID");
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testGetTodoListItemById() {
        Optional<TodoListItem> fromDb = repository.getItemById("uuid2");
        assertTrue(fromDb.isPresent());
    }

    @Test
    public void testGetTodoListItemByIdIsNull() {
        Optional<TodoListItem> fromDb = repository.getItemById("NOT_EXISTING_UUID");
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testTodoListHeaderInsert() {
        TodoListHeader header = createHeader();
        boolean success = repository.insert(header);
        assertTrue(success);
        TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", header.getUuid()).findFirst();
        assertNotNull(dao);
    }

    @Test
    public void testTodoListHeaderInsertTwice() {
        clearRealm();
        TodoListHeader header = createHeader();
        repository.insert(header);
        boolean success = repository.insert(header);
        assertFalse(success);
        TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", header.getUuid()).findFirst();
        assertNotNull(dao);
        fillRealm();
    }

    @Test
    public void testTodoListItemInsert() {
        TodoListItem item = createItem();
        boolean success = repository.insert(item);
        assertTrue(success);
        TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", item.getUuid()).findFirst();
        assertNotNull(dao);
    }

    @Test
    public void testTodoListItemInsertTwice() {
        clearRealm();
        TodoListItem item = createItem();
        repository.insert(item);
        boolean success = repository.insert(item);
        assertFalse(success);
        TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", item.getUuid()).findFirst();
        assertNotNull(dao);
        fillRealm();
    }

    @Test
    public void testHeaderDelete() {
        TodoListHeader header = createHeader();
        repository.insert(header);
        repository.delete(header);

        Optional<TodoListHeader> fromDb = repository.getHeaderById(header.getUuid());
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testDeleteHeaderNotExisting() {
        TodoListHeader header = createHeader();
        repository.delete(header);

        Optional<TodoListHeader> fromDb = repository.getHeaderById(header.getUuid());
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testItemDelete() {
        TodoListItem item = createItem();
        repository.insert(item);
        repository.delete(item);

        Optional<TodoListItem> fromDb = repository.getItemById(item.getUuid());
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testDeleteItemNotExisting() {
        TodoListItem item = createItem();
        repository.delete(item);

        Optional<TodoListItem> fromDb = repository.getItemById(item.getUuid());
        assertFalse(fromDb.isPresent());
    }

    @Test
    public void testUpdateHeader() throws InterruptedException {
        TodoListHeader header = createHeader();
        repository.insert(header);
        Thread.sleep(200);
        repository.update(header.update("New Title", 0, false));
        Optional<TodoListHeader> fromDb = repository.getHeaderById(header.getUuid());
        assertEquals(header.getCreatedAt(), fromDb.get().getCreatedAt());
        assertNotEquals(header.getChangedAt(), fromDb.get().getChangedAt());
        assertNotEquals(header.getTitle(), fromDb.get().getTitle());
    }

    @Test
    public void testUpdateHeaderAsInsert() {
        TodoListHeader header = createHeader();
        repository.update(header);
        Optional<TodoListHeader> fromDb = repository.getHeaderById(header.getUuid());
        assertEquals(header, fromDb.get());
    }

    @Test
    public void testUpdateItem() throws InterruptedException {
        TodoListItem item = createItem();
        repository.insert(item);
        Thread.sleep(200);
        repository.update(item.update("New Title", 0, false, "", false ,"uuid1"));
        Optional<TodoListItem> fromDb = repository.getItemById(item.getUuid());
        assertEquals(item.getCreatedAt(), fromDb.get().getCreatedAt());
        assertNotEquals(item.getChangedAt(), fromDb.get().getChangedAt());
        assertNotEquals(item.getTitle(), fromDb.get().getTitle());
    }

    @Test
    public void testUpdateItemAsInsert() {
        TodoListItem item = new TodoListItem("InsertTest", 0, false, "", "uuid0" ,"uuid1");
        repository.update(item);
        Optional<TodoListItem> fromDb = repository.getItemById(item.getUuid());
        assertEquals(item, fromDb.get());
    }
}