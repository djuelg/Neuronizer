package de.djuelg.neuronizer.storage;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.djuelg.neuronizer.domain.model.Color;
import de.djuelg.neuronizer.domain.model.Deadline;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.storage.migration.RealmMigrator;
import de.djuelg.neuronizer.storage.model.DeadlineDAO;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Domi on 13.07.2017.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class TodoListRepositoryImplTest {

    public final static RealmConfiguration TEST_REALM_CONFIG = new RealmConfiguration.Builder()
            .name("TEST_REALM_CONFIG")
            .schemaVersion(0)
            .migration(new RealmMigrator())
            .inMemory()
            .build();

    private Realm realm;
    private TodoListRepositoryImpl repository;

    @Before
    public void setUp() throws Exception {
        realm = Realm.getInstance(TEST_REALM_CONFIG);
        repository = new TodoListRepositoryImpl(realm);

        fillRealm();
    }

    private void fillRealm() {
        final TodoListDAO todoListDAO = new TodoListDAO("uuid0", "Todo List 1", 0, 0, 0);
        final TodoListHeaderDAO headerDAO = new TodoListHeaderDAO("uuid1", "Header 1", 0, 0, 0, 0, "uuid0");
        final TodoListItemDAO itemDAO = new TodoListItemDAO("uuid2", "Item 1", 0, 0, 0, new DeadlineDAO(), false, "", "uuid0", "uuid1");

        final TodoListHeaderDAO headerDAO2 = new TodoListHeaderDAO("uuid3", "Header 2", 0, 0, 0, 0, "uuid3");
        final TodoListItemDAO itemDAO2 = new TodoListItemDAO("uuid4", "Item 2", 0, 0, 0, new DeadlineDAO(), false, "", "uuid0", "uuid3");
        final TodoListItemDAO itemDAO3 = new TodoListItemDAO("uuid5", "Item 3", 0, 0, 0, new DeadlineDAO(), false, "", "uuid0", "uuid3");

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

    @After
    public void tearDown() throws Exception {
        clearRealm();
        realm.close();
    }

    @Test
    public void testGetTodoListById() {
        TodoList fromDb = repository.getTodoListById("uuid0");
        assertNotNull(fromDb);
    }

    @Test
    public void testGetTodoListByIdIsNull() {
        TodoList fromDb = repository.getTodoListById("NOT_EXISTING_UUID");
        assertNull(fromDb);
    }

    @Test
    public void testGetTodoListHeaderById() {
        TodoListHeader fromDb = repository.getHeaderById("uuid1");
        assertNotNull(fromDb);
    }

    @Test
    public void testGetTodoListHeaderByIdIsNull() {
        TodoListHeader fromDb = repository.getHeaderById("NOT_EXISTING_UUID");
        assertNull(fromDb);
    }

    @Test
    public void testGetTodoListItemById() {
        TodoListItem fromDb = repository.getItemById("uuid2");
        assertNotNull(fromDb);
    }

    @Test
    public void testGetTodoListItemByIdIsNull() {
        TodoListItem fromDb = repository.getItemById("NOT_EXISTING_UUID");
        assertNull(fromDb);
    }

    @Test
    public void testTodoListHeaderInsert() {
        TodoListHeader header = new TodoListHeader("InsertTest", 0, new Color(0), "uuid0");
        boolean success = repository.insert(header);
        assertTrue(success);
        TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", header.getUuid()).findFirst();
        assertNotNull(dao);
    }

    @Test
    public void testTodoListHeaderInsertTwice() {
        clearRealm();
        TodoListHeader header = new TodoListHeader("InsertTest", 0, new Color(0), "uuid0");
        repository.insert(header);
        boolean success = repository.insert(header);
        assertFalse(success);
        TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", header.getUuid()).findFirst();
        assertNotNull(dao);
        fillRealm();
    }

    @Test
    public void testTodoListItemInsert() {
        TodoListItem item = new TodoListItem("InsertTest", 0, new Deadline(), false, "", "uuid0" ,"uuid1");
        boolean success = repository.insert(item);
        assertTrue(success);
        TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", item.getUuid()).findFirst();
        assertNotNull(dao);
    }

    @Test
    public void testTodoListItemInsertTwice() {
        clearRealm();
        TodoListItem item = new TodoListItem("InsertTest", 0, new Deadline(), false, "", "uuid0" ,"uuid1");
        repository.insert(item);
        boolean success = repository.insert(item);
        assertFalse(success);
        TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", item.getUuid()).findFirst();
        assertNotNull(dao);
        fillRealm();
    }

    @Test
    public void testHeaderDelete() {
        TodoListHeader header = new TodoListHeader("InsertTest", 0, new Color(0), "uuid0");
        repository.insert(header);
        repository.delete(header);

        TodoListHeader fromDb = repository.getHeaderById(header.getUuid());
        assertNull(fromDb);
    }

    @Test
    public void testDeleteHeaderNotExisting() {
        TodoListHeader header = new TodoListHeader("InsertTest", 0, new Color(0), "uuid0");
        repository.delete(header);

        TodoListHeader fromDb = repository.getHeaderById(header.getUuid());
        assertNull(fromDb);
    }

    @Test
    public void testItemDelete() {
        TodoListItem item = new TodoListItem("InsertTest", 0, new Deadline(), false, "", "uuid0" ,"uuid1");
        repository.insert(item);
        repository.delete(item);

        TodoListItem fromDb = repository.getItemById(item.getUuid());
        assertNull(fromDb);
    }

    @Test
    public void testDeleteItemNotExisting() {
        TodoListItem item = new TodoListItem("InsertTest", 0, new Deadline(), false, "", "uuid0" ,"uuid1");
        repository.delete(item);

        TodoListItem fromDb = repository.getItemById(item.getUuid());
        assertNull(fromDb);
    }

    @Test
    public void testUpdateHeader() throws InterruptedException {
        TodoListHeader header = new TodoListHeader("InsertTest", 0, new Color(0), "uuid0");
        repository.insert(header);
        Thread.sleep(200);
        repository.update(header.update("New Title", 0, new Color(0), "uuid0"));
        TodoListHeader fromDb = repository.getHeaderById(header.getUuid());
        assertEquals(header.getCreatedAt(), fromDb.getCreatedAt());
        assertNotEquals(header.getChangedAt(), fromDb.getChangedAt());
        assertNotEquals(header.getTitle(), fromDb.getTitle());
    }

    @Test
    public void testUpdateHeaderAsInsert() {
        TodoListHeader header = new TodoListHeader("InsertTest", 0, new Color(0), "uuid0");
        repository.update(header);
        TodoListHeader fromDb = repository.getHeaderById(header.getUuid());
        assertEquals(header, fromDb);
    }

    @Test
    public void testUpdateItem() throws InterruptedException {
        TodoListItem item = new TodoListItem("InsertTest", 0, new Deadline(), false, "", "uuid0" ,"uuid1");
        repository.insert(item);
        Thread.sleep(200);
        repository.update(item.update("New Title", 0, new Deadline(), false, "", "uuid0" ,"uuid1"));
        TodoListItem fromDb = repository.getItemById(item.getUuid());
        assertEquals(item.getCreatedAt(), fromDb.getCreatedAt());
        assertNotEquals(item.getChangedAt(), fromDb.getChangedAt());
        assertNotEquals(item.getTitle(), fromDb.getTitle());
    }

    @Test
    public void testUpdateItemAsInsert() {
        TodoListItem item = new TodoListItem("InsertTest", 0, new Deadline(), false, "", "uuid0" ,"uuid1");
        repository.update(item);
        TodoListItem fromDb = repository.getItemById(item.getUuid());
        assertEquals(item, fromDb);
    }
}