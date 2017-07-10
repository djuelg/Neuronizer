package de.djuelg.neuronizer.domain.interactors;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;
import de.djuelg.neuronizer.domain.interactors.impl.DisplayPreviewInteractorImpl;
import de.djuelg.neuronizer.domain.model.Color;
import de.djuelg.neuronizer.domain.model.Deadline;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.mockito.Mockito.when;


/**
 * Tests our DisplayPreviewInteractor.
 */
public class DisplayPreviewInteractorTest {

    private       MainThread                   mMainThread;
    @Mock private Executor                     mExecutor;
    @Mock private TodoListRepository mTodoListRepository;
    @Mock private DisplayPreviewInteractor.Callback mMockedCallback;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void testNoPreviewExisting() throws Exception {
        DisplayPreviewInteractorImpl interactor = new DisplayPreviewInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository);
        interactor.run();

        Mockito.when(mTodoListRepository.getPreviews())
                .thenReturn(null);

        Mockito.verify(mTodoListRepository).getPreviews();
        Mockito.verifyNoMoreInteractions(mTodoListRepository);
        Mockito.verify(mMockedCallback).onRetrievalFailed(ExceptionId.NO_LISTS);
    }

    @Test
    public void testPreviewFound() throws Exception {

        List<TodoListPreview> previews = new ArrayList<>(1);
        previews.add(createPreview());

        when(mTodoListRepository.getPreviews())
                .thenReturn(previews);

        DisplayPreviewInteractorImpl interactor = new DisplayPreviewInteractorImpl(mExecutor, mMainThread, mMockedCallback, mTodoListRepository);
        interactor.run();

        Mockito.verify(mTodoListRepository).getPreviews();
        Mockito.verifyNoMoreInteractions(mTodoListRepository);
        Mockito.verify(mMockedCallback).onPreviewsRetrieved(previews);
    }

    private TodoListPreview createPreview() {
        TodoList todoList = new TodoList("Hallo", 0);
        TodoListHeader header = new TodoListHeader("2","Header1", new Date(), new Date(), 0, new Color(1), "1");
        TodoListItem item = new TodoListItem("3", "Item1", new Date(), new Date(), 0, new Deadline(new Date()), false, "1", "2");
        List<TodoListItem> items = new ArrayList<>(1);
        items.add(item);

        return new TodoListPreview(todoList, header, items);
    }
}
