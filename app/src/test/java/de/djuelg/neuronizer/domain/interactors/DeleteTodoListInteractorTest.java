package de.djuelg.neuronizer.domain.interactors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.impl.DeleteTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 11.07.17.
 */
public class DeleteTodoListInteractorTest {

    private MainThread mMainThread;
    private TodoListRepository mTodoListRepository;
    @Mock private Executor mExecutor;
    @Mock private DeleteTodoListInteractor.Callback mMockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
        mTodoListRepository = new TodoListRepositoryMock();
    }

    @Test
    public void testInsertViaUpdate() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        DeleteTodoListInteractorImpl interactor = new DeleteTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository, todoList.getUuid());
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) mTodoListRepository;
        assertEquals(repositoryMock.deleteCount, 1);
        Mockito.verify(mMockedCallback).onTodoListDeleted(any(TodoList.class));
    }
}