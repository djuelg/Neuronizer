package de.djuelg.neuronizer.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.djuelg.neuronizer.domain.model.Color;
import de.djuelg.neuronizer.domain.model.Deadline;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by dmilicic on 1/29/16.
 */
public class TodoListRepositoryImpl implements TodoListRepository {
    @Override
    public Iterable<TodoListPreview> getPreviews() {
        // TODO get items from realm
        List<TodoListPreview> previews = new ArrayList<>(1);
        previews.add(createPreview());

        // let's simulate some network/database lag
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return previews;
    }

    @Override
    public boolean insert(TodoList todoList) {
        // TODO implement
        return true;
    }

    // TODO REMOVE
    private TodoListPreview createPreview() {
        TodoList todoList = new TodoList("Hallo", 0);
        TodoListHeader header = new TodoListHeader("2","Header1", new Date(), new Date(), 0, new Color(1), "1");
        TodoListItem item = new TodoListItem("3", "Item1", new Date(), new Date(), 0, new Deadline(new Date()), false, "1", "2");
        List<TodoListItem> items = new ArrayList<>(1);
        items.add(item);

        return new TodoListPreview(todoList, header, items);
    }
}
