package de.djuelg.neuronizer.presentation.ui.flexibleadapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.todolist.TodoListHeader;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractExpandableHeaderItem;
import eu.davidea.viewholders.ExpandableViewHolder;

/**
 * Created by djuelg on 20.07.17.
 */

public class TodoListHeaderViewModel extends AbstractExpandableHeaderItem<TodoListHeaderViewModel.ViewHolder, TodoListItemViewModel> {

    private final TodoListHeader header;

    public TodoListHeaderViewModel(TodoListHeader header) {
        this.header = Objects.requireNonNull(header);
    }

    @Override
    public boolean isSwipeable() {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.todo_list_header;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.title.setText(header.getTitle());
        holder.expansion.setImageResource(isExpanded()
                ? R.drawable.ic_expand_less_black_24dp
                : R.drawable.ic_expand_more_black_24dp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoListHeaderViewModel)) return false;
        TodoListHeaderViewModel that = (TodoListHeaderViewModel) o;
        return Objects.equals(header, that.header);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header);
    }

    /**
     * Needed ViewHolder
     */
    class ViewHolder extends ExpandableViewHolder {

        @Bind(R.id.todo_list_header_title) TextView title;
        @Bind(R.id.todo_list_header_expand) ImageView expansion;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true); // sticky = true
            ButterKnife.bind(this, view);
        }

    }
}
