package de.djuelg.neuronizer.storage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;

/**
 * Created by dmilicic on 1/29/16.
 */
public class TodoListRepositoryImpl implements TodoListRepository {
    @Override
    public Iterable<TodoList> getAllLists() {
        // TODO get items from realm
        List<TodoList> lists = new ArrayList<>(1);
        lists.add(new TodoList("1","Hallo", new Date(), new Date(), 0));

        // let's simulate some network/database lag
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return lists;
    }
}
