package de.djuelg.neuronizer.domain.interactors.todolist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.AddHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by Domi on 14.07.2017.
 */
public class AddHeaderInteractorTest {

    private MainThread mainThread;
    private TodoListRepository repository;
    @Mock private Executor executor;
    @Mock private AddHeaderInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new TodoListRepositoryMock();
    }

    @Test
    public void testSuccessfulAdd() throws Exception {
        AddHeaderInteractorImpl interactor = new AddHeaderInteractorImpl(executor, mainThread, mockedCallback, repository, "Header", 0, 0, "todo-id");
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(1, repositoryMock.insertCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback).onHeaderAdded();
    }

    @Test
    public void testRetryOnDuplicatedUuid() throws Exception {
        AddHeaderInteractorImpl interactor = new AddHeaderInteractorImpl(executor, mainThread, mockedCallback, repository, "HEADER_DUPLICATION", 0, 0, "todo-id");
        interactor.run();
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertTrue("insertCount should be called 3 times because one failure occurs", repositoryMock.insertCount >= 3);
        assertEquals(2, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback, Mockito.times(2)).onHeaderAdded();
    }

    @Test
    public void testParentNotExisting() throws Exception {
        AddHeaderInteractorImpl interactor = new AddHeaderInteractorImpl(executor, mainThread, mockedCallback, repository, "Header", 0, 0, "MISSING_UUID");
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(0, repositoryMock.insertCount);
        assertEquals(0, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback).onParentNotFound();
    }
}