package de.djuelg.neuronizer.domain.interactors;


import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.impl.DisplayAllListsInteractorImpl;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.threading.TestMainThread;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Tests our DisplayAllListsInteractor.
 */
public class DisplayAllListsInteractorTest {

    private       MainThread                   mMainThread;
    @Mock private Executor                     mExecutor;
    @Mock private TodoListRepository mTodoListRepository;
    @Mock private DisplayAllListsInteractor.Callback mMockedCallback;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void testNoTodoListsExisting() throws Exception {
        DisplayAllListsInteractorImpl interactor = new DisplayAllListsInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository);
        interactor.run();

        Mockito.when(mTodoListRepository.getAllLists())
                .thenReturn(null);

        Mockito.verify(mTodoListRepository).getAllLists();
        Mockito.verifyNoMoreInteractions(mTodoListRepository);
        Mockito.verify(mMockedCallback).onRetrievalFailed(anyString());
    }

    @Test
    public void testTodoListFound() throws Exception {

        List<TodoList> lists = new ArrayList<>(1);
        lists.add(new TodoList("1","Hallo", new Date(), new Date(), 0));

        when(mTodoListRepository.getAllLists())
                .thenReturn(lists);

        DisplayAllListsInteractorImpl interactor = new DisplayAllListsInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository);
        interactor.run();

        Mockito.verify(mTodoListRepository).getAllLists();
        Mockito.verifyNoMoreInteractions(mTodoListRepository);
        Mockito.verify(mMockedCallback).onAllListsRetrieved(lists);
    }
}
