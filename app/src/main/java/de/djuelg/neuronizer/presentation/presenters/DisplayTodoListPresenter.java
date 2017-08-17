package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IHeader;


public interface DisplayTodoListPresenter extends BasePresenter {

    void loadTodoList(String uuid);

    void syncTodoList(List<IHeader> headerItems);

    void deleteHeader(String uuid);

    void deleteItem(String uuid);

    interface View {
        void onTodoListLoaded(List<AbstractFlexibleItem> items);

        void onTodoListReloaded(List<AbstractFlexibleItem> items);
    }
}
