package de.djuelg.neuronizer.domain.interactors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.impl.AddTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by djuelg on 09.07.17.
 */
public class AddTodoListInteractorTest {

    private MainThread mMainThread;
    private TodoListRepository mTodoListRepository;
    @Mock private Executor mExecutor;
    @Mock private AddTodoListInteractor.Callback mMockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
        mTodoListRepository = new TodoListRepositoryMock();
    }

    @Test
    public void testSuccessfulAdd() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository, todoList);
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) mTodoListRepository;
        assertEquals(repositoryMock.insertCount, 1);
        assertEquals(repositoryMock.uuids.size(), 1);
        Mockito.verify(mMockedCallback).onTodoListAdded();
    }

    @Test
    public void testRetryOnDuplicatedUuid() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository, todoList);
        interactor.run();
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) mTodoListRepository;
        assertTrue(repositoryMock.insertCount >= 3);
        assertEquals(repositoryMock.uuids.size(), 2);
        Mockito.verify(mMockedCallback, Mockito.times(2)).onTodoListAdded();
    }
}