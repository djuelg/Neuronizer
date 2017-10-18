package de.djuelg.neuronizer.domain.model.preview;

import java.util.List;

import de.djuelg.neuronizer.domain.repository.Repository;

/**
 * Created by djuelg on 18.10.17.
 */

public class Importance {

    private static final int ACCESS_PEAK = 180;

    public static void increase(Repository repository, ImportanceComparable importance) {
        if (importance instanceof TodoList) {
            final TodoList todoList = (TodoList) importance.increaseAccessCounter();
            repository.todoList().update(todoList);

        } else if (importance instanceof Note) {
            final Note note = (Note) importance.increaseAccessCounter();
            repository.note().update(note);
        }
    }

    public static void checkForNormalization(Repository repository, ImportanceComparable importance) {
        if (importance.getAccessCounter() >= ACCESS_PEAK) normalizeImportance(repository);
    }

    private static void normalizeImportance(Repository repository) {
        normalizeTodoListImportance(repository);
        normalizeNoteImportance(repository);
    }

    private static void normalizeNoteImportance(Repository repository) {
        List<Note> notes = repository.note().getAll();

        for (Note note : notes) {
            repository.note().update(note.normalizeAccessCounter());
        }
    }

    private static void normalizeTodoListImportance(Repository repository) {
        List<TodoList> todoLists = repository.todoList().getAll();

        for (TodoList todoList : todoLists) {
            repository.todoList().update(todoList.normalizeAccessCounter());
        }
    }
}
