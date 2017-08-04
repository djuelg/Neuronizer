package de.djuelg.neuronizer.storage.converter;

import java.util.Date;

import de.djuelg.neuronizer.domain.model.preview.TodoList;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;

/**
 * Created by djuelg on 11.07.17.
 */

public class RealmConverter {

    // domain to dao

    public static TodoListDAO convert(TodoList todoList) {
        return new TodoListDAO(
                todoList.getUuid(),
                todoList.getTitle(),
                todoList.getCreatedAt().getTime(),
                todoList.getChangedAt().getTime(),
                todoList.getPosition()
        );
    }

    public static TodoListHeaderDAO convert(TodoListHeader header) {
        return new TodoListHeaderDAO(
                header.getUuid(),
                header.getTitle(),
                header.getCreatedAt().getTime(),
                header.getChangedAt().getTime(),
                header.getPosition(),
                header.getParentTodoListUuid()
        );
    }

    public static TodoListItemDAO convert(TodoListItem item) {
        return new TodoListItemDAO(
                item.getUuid(),
                item.getTitle(),
                item.getCreatedAt().getTime(),
                item.getChangedAt().getTime(),
                item.getPosition(),
                item.isImportant(),
                item.getDetails(),
                item.isDone(),
                item.getParentTodoListUuid(),
                item.getParentHeaderUuid()
        );
    }

    // dao to domain

    public static TodoList convert(TodoListDAO listDAO) {
        return new TodoList(
                listDAO.getUuid(),
                listDAO.getTitle(),
                new Date(listDAO.getCreatedAt()),
                new Date(listDAO.getChangedAt()),
                listDAO.getPosition()
        );
    }

    public static TodoListHeader convert(TodoListHeaderDAO headerDAO) {
        return new TodoListHeader(
                headerDAO.getUuid(),
                headerDAO.getTitle(),
                new Date(headerDAO.getCreatedAt()),
                new Date(headerDAO.getChangedAt()),
                headerDAO.getPosition(),
                headerDAO.getParentTodoListUuid()
        );
    }

    public static TodoListItem convert(TodoListItemDAO itemDAO) {
        return new TodoListItem(
                itemDAO.getUuid(),
                itemDAO.getTitle(),
                new Date(itemDAO.getCreatedAt()),
                new Date(itemDAO.getChangedAt()),
                itemDAO.getPosition(),
                itemDAO.isImportant(),
                itemDAO.getDetails(),
                itemDAO.isDone(),
                itemDAO.getParentTodoListUuid(),
                itemDAO.getParentHeaderUuid());
    }
}
