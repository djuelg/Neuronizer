package de.djuelg.neuronizer.presentation.presenters;

import java.util.List;

import de.djuelg.neuronizer.presentation.presenters.base.BasePresenter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;


public interface DisplayTodoListPresenter extends BasePresenter {

    void loadTodoList(String uuid);

    interface View {
        void onTodoListLoaded(List<AbstractFlexibleItem> items);

        void onRetrievalFailed();
    }
}
