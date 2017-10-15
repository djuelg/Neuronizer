package de.djuelg.neuronizer.storage;

import com.fernandocejas.arrow.optional.Optional;

import java.lang.reflect.Field;
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
    public Iterable<Preview> getPreviews(ItemsPerPreview itemsPerPreview) {
        return new ArrayList<>(0);
    }

    @Override
    public Optional<TodoList> getTodoListById(String uuid) {
        return Optional.of(alwaysSameTodoList);
    }

    @Override
    public Optional<Note> getNoteById(String uuid) {
        return Optional.of(alwaysSameNote);
    }

    @Override
    // TODO Test
    public int getNumberOfPreviews() {
        return 42;
    }

    @Override
    public boolean insert(TodoList todoList) {
        // a little bit of java reflection to check for duplicated uuid
        if ("TODO_LIST_DUPLICATION".equals(todoList.getTitle()) && insertCount <= 1) {
            try {
                Field uuidField = todoList.getClass().getDeclaredField("uuid");
                uuidField.setAccessible(true);
                uuidField.set(todoList, "DUPLICATED_UUID");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        insertCount++;
        boolean isNewUuid = !uuids.contains(todoList.getUuid());
        uuids.add(todoList.getUuid());
        return isNewUuid;
    }

    @Override
    public boolean insert(Note note) {
        // a little bit of java reflection to check for duplicated uuid
        if ("TODO_LIST_DUPLICATION".equals(note.getTitle()) && insertCount <= 1) {
            try {
                Field uuidField = note.getClass().getDeclaredField("uuid");
                uuidField.setAccessible(true);
                uuidField.set(note, "DUPLICATED_UUID");

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        insertCount++;
        boolean isNewUuid = !uuids.contains(note.getUuid());
        uuids.add(note.getUuid());
        return isNewUuid;
    }

    @Override
    public void update(TodoList updatedItem) {
        updateCount++;
        uuids.add(updatedItem.getUuid());
    }

    @Override
    public void update(Note updatedNote) {
        updateCount++;
        uuids.add(updatedNote.getUuid());
    }

    @Override
    public void delete(TodoList deletedItem) {
        deleteCount++;
    }

    @Override
    public void delete(Note deletedNote) {
        deleteCount++;
    }
}
