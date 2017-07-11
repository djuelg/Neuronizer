package de.djuelg.neuronizer.domain.interactors.preview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.impl.EditTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.PreviewRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 10.07.17.
 */
public class EditTodoListInteractorTest {

    private MainThread mMainThread;
    private PreviewRepository mPreviewRepository;
    @Mock private Executor mExecutor;
    @Mock private EditTodoListInteractor.Callback mMockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
        mPreviewRepository = new PreviewRepositoryMock();
    }

    @After
    public void tearDown() throws Exception {
        mPreviewRepository.close();
        assertTrue(((PreviewRepositoryMock)mPreviewRepository).closedCalled);
    }

    @Test
    public void testInsertViaUpdate() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        EditTodoListInteractorImpl interactor = new EditTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mPreviewRepository, todoList.getUuid(), todoList.getTitle(), todoList.getPosition());
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) mPreviewRepository;
        assertEquals(repositoryMock.updateCount, 1);
        assertEquals(repositoryMock.uuids.size(), 1);
        Mockito.verify(mMockedCallback).onTodoListUpdated(any(TodoList.class));
    }

    @Test
    public void testRealUpdate() throws Exception {
        TodoList todoList = new TodoList("TodoList1", 0);
        EditTodoListInteractorImpl interactor = new EditTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mPreviewRepository, todoList.getUuid(), todoList.getTitle(), todoList.getPosition());
        interactor.run();
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) mPreviewRepository;
        assertEquals(2, repositoryMock.updateCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mMockedCallback, Mockito.atLeastOnce()).onTodoListUpdated(any(TodoList.class));
    }
}