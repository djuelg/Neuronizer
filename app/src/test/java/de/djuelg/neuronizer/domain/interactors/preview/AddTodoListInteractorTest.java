package de.djuelg.neuronizer.domain.interactors.preview;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.preview.impl.AddTodoListInteractorImpl;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;
import de.djuelg.neuronizer.storage.PreviewRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by djuelg on 09.07.17.
 */
public class AddTodoListInteractorTest {

    private MainThread mMainThread;
    private PreviewRepository mPreviewRepository;
    @Mock private Executor mExecutor;
    @Mock private AddTodoListInteractor.Callback mMockedCallback;

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
    public void testSuccessfulAdd() throws Exception {
        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mPreviewRepository, "TodoList1", 0);
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) mPreviewRepository;
        assertEquals(1, repositoryMock.insertCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mMockedCallback).onTodoListAdded();
    }

//    Failing because uuid can not be influenced. Create constructor for testing?
//    @Test
//    public void testRetryOnDuplicatedUuid() throws Exception {
//        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(mExecutor, mMainThread, mMockedCallback, mPreviewRepository, "TodoList1", 0);
//        interactor.run();
//        interactor.run();
//
//        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) mPreviewRepository;
//        assertTrue(repositoryMock.insertCount >= 3);
//        assertEquals(2, repositoryMock.uuids.size());
//        Mockito.verify(mMockedCallback, Mockito.times(2)).onTodoListAdded();
//    }
}