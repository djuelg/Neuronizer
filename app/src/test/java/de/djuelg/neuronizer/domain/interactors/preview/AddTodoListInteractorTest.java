package de.djuelg.neuronizer.domain.interactors.preview;

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

    private MainThread mainThread;
    private PreviewRepository repository;
    @Mock private Executor executor;
    @Mock private AddTodoListInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new PreviewRepositoryMock();
    }

    @Test
    public void testSuccessfulAdd() throws Exception {
        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(executor, mainThread, mockedCallback, repository, "TodoList1");
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) repository;
        assertEquals(1, repositoryMock.insertCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback).onTodoListAdded();
    }

    @Test
    public void testRetryOnDuplicatedUuid() throws Exception {
        AddTodoListInteractorImpl interactor = new AddTodoListInteractorImpl(executor, mainThread, mockedCallback, repository, "TODO_LIST_DUPLICATION");
        interactor.run();
        interactor.run();

        PreviewRepositoryMock repositoryMock = (PreviewRepositoryMock) repository;
        assertTrue("insertCount should be called 3 times because one failure occurs", repositoryMock.insertCount >= 3);
        assertEquals(2, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback, Mockito.times(2)).onTodoListAdded();
    }
}