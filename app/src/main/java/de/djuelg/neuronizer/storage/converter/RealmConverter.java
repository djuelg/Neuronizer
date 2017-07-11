package de.djuelg.neuronizer.storage.converter;

import java.util.Date;

import de.djuelg.neuronizer.domain.model.Color;
import de.djuelg.neuronizer.domain.model.Deadline;
import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;

/**
 * Created by djuelg on 11.07.17.
 */

public class RealmConverter {

    public static TodoListDAO convert(TodoList todoList) {
        return new TodoListDAO(
                todoList.getUuid(),
                todoList.getTitle(),
                todoList.getCreatedAt().getTime(),
                todoList.getChangedAt().getTime(),
                todoList.getPosition()
        );
    }

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
                new Color(headerDAO.getColor()),
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
                new Deadline(itemDAO.getDeadlineAt()),
                itemDAO.isImportant(),
                itemDAO.getDetails(),
                itemDAO.getParentTodoListUuid(),
                itemDAO.getParentHeaderUuid()
        );
    }
}
