package de.djuelg.neuronizer.storage;

import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.fernandocejas.arrow.collections.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.Preview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static de.djuelg.neuronizer.storage.RepositoryManager.SCHEMA_VERSION;
import static org.junit.Assert.assertEquals;

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
        Iterable<Preview> previews = repository.getAll(new ItemsPerPreview(0));
        assertEquals(false, previews.iterator().hasNext());
        fillRealm();
    }

    @Test
    public void testGetPreviewCorrectHeader() {
        Iterable<Preview> previews = repository.getAll(new ItemsPerPreview(0));
        assertEquals("Header 1", previews.iterator().next().getSubtitle());
    }

    @Test
    public void testGetPreviewManyItemsPerPreview() {
        Iterable<Preview> previews = repository.getAll(new ItemsPerPreview(100));
        assertEquals("Header 1", previews.iterator().next().getSubtitle());
        assertEquals(1, Lists.newArrayList(previews).size());
    }
}