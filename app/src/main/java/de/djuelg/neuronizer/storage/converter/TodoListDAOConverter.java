package de.djuelg.neuronizer.storage.converter;


import com.fernandocejas.arrow.functions.Function;

import org.jetbrains.annotations.Nullable;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.storage.model.TodoListDAO;

/**
 * Created by djuelg on 04.08.17.
 */

public class TodoListDAOConverter implements Function<TodoListDAO, TodoList> {

    @Nullable
    @Override
    public TodoList apply(TodoListDAO input) {
        return RealmConverter.convert(input);
    }
}
