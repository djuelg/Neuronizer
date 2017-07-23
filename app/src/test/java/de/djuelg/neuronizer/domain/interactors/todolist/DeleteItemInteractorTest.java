package de.djuelg.neuronizer.domain.interactors.todolist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.DeleteItemInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 11.07.17.
 */
public class DeleteItemInteractorTest {

    private MainThread mainThread;
    private TodoListRepository repository;
    @Mock private Executor executor;
    @Mock private DeleteItemInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new TodoListRepositoryMock();
    }

    @Test
    public void testDelete() throws Exception {
        DeleteItemInteractorImpl interactor = new DeleteItemInteractorImpl(executor, mainThread, mockedCallback, repository, "item-uuid");
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(repositoryMock.deleteCount, 1);
        Mockito.verify(mockedCallback).onItemDeleted(any(TodoListItem.class));
    }
}