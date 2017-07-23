package de.djuelg.neuronizer.domain.interactors.todolist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DeleteHeaderInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 11.07.17.
 */
public class DeleteHeaderInteractorTest {

    private MainThread mainThread;
    private TodoListRepository repository;
    @Mock private Executor executor;
    @Mock private DeleteHeaderInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new TodoListRepositoryMock();
    }

    @Test
    public void testDelete() throws Exception {
        DeleteHeaderInteractorImpl interactor = new DeleteHeaderInteractorImpl(executor, mainThread, mockedCallback, repository, "header-uuid");
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(repositoryMock.deleteCount, 1);
        Mockito.verify(mockedCallback).onHeaderDeleted(any(TodoListHeader.class));
    }
}