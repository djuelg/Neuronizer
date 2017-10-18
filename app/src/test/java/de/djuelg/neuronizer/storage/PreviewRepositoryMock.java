package de.djuelg.neuronizer.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.djuelg.neuronizer.domain.model.preview.ItemsPerPreview;
import de.djuelg.neuronizer.domain.model.preview.Note;
import de.djuelg.neuronizer.domain.model.preview.Preview;
import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.repository.PreviewRepository;

/**
 * Created by djuelg on 10.07.17.
 */

public class PreviewRepositoryMock implements PreviewRepository {

    public int insertCount;
    public int updateCount;
    public int deleteCount;
    public Set<String> uuids;
    private TodoList alwaysSameTodoList;
    private Note alwaysSameNote;

    public PreviewRepositoryMock() {
        insertCount = 0;
        updateCount = 0;
        deleteCount = 0;
        uuids = new HashSet<>();
        alwaysSameTodoList = new TodoList("In-Database", 0);
        alwaysSameNote = new Note("Note", 0);
    }

    @Override
    public Iterable<Preview> getAll(ItemsPerPreview itemsPerPreview) {
        return new ArrayList<>(0);
    }

    @Override
    // TODO Test
    public int count() {
        return 42;
    }
}
