package de.djuelg.neuronizer.domain.interactors.todolist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 10.07.17.
 */
public class EditHeaderInteractorTest {

    private MainThread mainThread;
    private TodoListRepository repository;
    @Mock private Executor executor;
    @Mock private EditHeaderInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new TodoListRepositoryMock();
    }

    @Test
    public void testInsertViaUpdate() throws Exception {
        EditHeaderInteractorImpl interactor = new EditHeaderInteractorImpl(executor, mainThread, mockedCallback, repository, "uuid", "title", 0, false);
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(repositoryMock.updateCount, 2);
        Mockito.verify(mockedCallback).onHeaderUpdated(any(TodoListHeader.class));
    }

    @Test
    public void testRealUpdate() throws Exception {
        EditHeaderInteractorImpl interactor = new EditHeaderInteractorImpl(executor, mainThread, mockedCallback, repository, "uuid", "title", 0, false);
        interactor.run();
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(4, repositoryMock.updateCount);
        Mockito.verify(mockedCallback, Mockito.atLeastOnce()).onHeaderUpdated(any(TodoListHeader.class));
    }
}