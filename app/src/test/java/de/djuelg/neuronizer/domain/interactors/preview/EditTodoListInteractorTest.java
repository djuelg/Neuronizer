package de.djuelg.neuronizer.domain.interactors.preview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.impl.EditTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.PreviewRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 10.07.17.
 */
public class EditTodoListInteractorTest {

    private MainThread mainThread;
    private PreviewRepository repository;
    @Mock private Executor executor;
    @Mock private EditTodoListInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new PreviewRepositoryMock();
    }

    @Test
    public void testInsertViaUpdate() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        EditTodoListInteractorImpl interactor = new EditTodoListInteractorImpl(executor, mainThread, mockedCallback, repository, todoList.getUuid(), todoList.getTitle(), todoList.getPosition());
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) repository;
        assertEquals(repositoryMock.updateCount, 1);
        assertEquals(repositoryMock.uuids.size(), 1);
        Mockito.verify(mockedCallback).onTodoListUpdated(any(TodoList.class));
    }

    @Test
    public void testRealUpdate() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        EditTodoListInteractorImpl interactor = new EditTodoListInteractorImpl(executor, mainThread, mockedCallback, repository, todoList.getUuid(), todoList.getTitle(), todoList.getPosition());
        interactor.run();
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) repository;
        assertEquals(2, repositoryMock.updateCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback, Mockito.atLeastOnce()).onTodoListUpdated(any(TodoList.class));
    }
}