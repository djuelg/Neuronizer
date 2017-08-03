package de.djuelg.neuronizer.domain.interactors.preview;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DeleteTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.PreviewRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 11.07.17.
 */
public class DeleteTodoListInteractorTest {

    private MainThread mainThread;
    private PreviewRepository repository;
    @Mock private Executor executor;
    @Mock private DeleteTodoListInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new PreviewRepositoryMock();
    }

    @Test
    public void testDelete() throws Exception {
        TodoList todoList = new TodoList("TodoList1");
        DeleteTodoListInteractorImpl interactor = new DeleteTodoListInteractorImpl(executor, mainThread, mockedCallback, repository, todoList.getUuid());
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) repository;
        assertEquals(repositoryMock.deleteCount, 1);
        Mockito.verify(mockedCallback).onTodoListDeleted(any(TodoList.class));
    }
}