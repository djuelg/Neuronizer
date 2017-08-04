package de.djuelg.neuronizer.domain.interactors.preview;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.impl.DisplayPreviewInteractorImpl;
import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.preview.TodoListPreview;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.mockito.Mockito.when;


/**
 * Tests our DisplayPreviewInteractor.
 */
public class DisplayPreviewInteractorTest {

    private MainThread mainThread;
    @Mock private Executor executor;
    @Mock private PreviewRepository repository;
    @Mock private DisplayPreviewInteractor.Callback mockedCallback;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
    }

    @Test
    @Ignore("testNoPreviewExisting: Fix test by replacing repository with custom mock")
    public void testNoPreviewExisting() throws Exception {
        DisplayPreviewInteractorImpl interactor = new DisplayPreviewInteractorImpl(executor, mainThread, mockedCallback, repository);
        interactor.run();

        // TODO Fix test by replacing repository with custom mock
        Mockito.when(repository.getPreviews(new ItemsPerPreview(2)))
                .thenReturn(new ArrayList<TodoListPreview>(0));

        Mockito.verify(repository).getPreviews(new ItemsPerPreview(2));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verify(mockedCallback).onPreviewsRetrieved(new ArrayList<TodoListPreview>(0));
    }

    @Test
    @Ignore("testNoPreviewExisting: Fix test by replacing repository with custom mock")
    public void testPreviewFound() throws Exception {

        List<TodoListPreview> previews = new ArrayList<>(1);
        previews.add(createPreview());

        // TODO Fix test by replacing repository with custom mock
        when(repository.getPreviews(new ItemsPerPreview(2)))
                .thenReturn(previews);

        DisplayPreviewInteractorImpl interactor = new DisplayPreviewInteractorImpl(executor, mainThread, mockedCallback, repository);
        interactor.run();

        Mockito.verify(repository).getPreviews(new ItemsPerPreview(4));
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verify(mockedCallback).onPreviewsRetrieved(previews);
    }

    private TodoListPreview createPreview() {
        TodoList todoList = new TodoList("Hallo");
        TodoListHeader header = new TodoListHeader("2","Header1", new Date(), new Date(), 0, false, "1");
        TodoListItem item = new TodoListItem("3", "Item1", new Date(), new Date(), 0, false, "1", false, "2", "");
        List<TodoListItem> items = new ArrayList<>(1);
        items.add(item);

        return new TodoListPreview(todoList, header, items);
    }
}
