package de.djuelg.neuronizer.presentation.ui.flexibleadapter;

import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by djuelg on 20.07.17.
 */

public class TodoListItemViewModel extends AbstractSectionableItem<TodoListItemViewModel.ViewHolder, TodoListHeaderViewModel> {

    private final TodoListItem item;

    public TodoListItemViewModel(TodoListHeaderViewModel headerVM, TodoListItem item) {
        super(headerVM);
        this.item = Objects.requireNonNull(item);
    }

    public TodoListItem getItem() {
        return item;
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.todo_list_item;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.title.setText(item.getTitle());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListItemViewModel)) return false;
        TodoListItemViewModel that = (TodoListItemViewModel) o;
        return Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    /**
     * Needed ViewHolder
     */
    class ViewHolder extends FlexibleViewHolder {

        @Bind(R.id.todo_list_item_title) TextView title;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}
