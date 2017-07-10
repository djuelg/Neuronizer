package de.djuelg.neuronizer.domain.interactors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.impl.AddTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;

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
        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository, "TodoList1", 0);
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) mTodoListRepository;
        assertEquals(1, repositoryMock.insertCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mMockedCallback).onTodoListAdded();
    }

//    Failing because uuid can not be influenced. Create constructor for testing?
//    @Test
//    public void testRetryOnDuplicatedUuid() throws Exception {
//        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository, "TodoList1", 0);
//        interactor.run();
//        interactor.run();
//
//        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) mTodoListRepository;
//        assertTrue(repositoryMock.insertCount >= 3);
//        assertEquals(2, repositoryMock.uuids.size());
//        Mockito.verify(mMockedCallback, Mockito.times(2)).onTodoListAdded();
//    }
}