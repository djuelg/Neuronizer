package de.djuelg.neuronizer.presentation.ui.flexibleadapter;

import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        this.setExpanded(header.isExpanded());
    }

    public TodoListHeader getHeader() {
        return header;
    }

    @Override
    public boolean isSwipeable() {
        return false;
    }

    @Override
    public boolean isSelectable() {
        return true;
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
        holder.expandImage.setImageResource(isExpanded()
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
    public String toString() {
        return header.getTitle();
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
        @Bind(R.id.todo_list_header_expand) ImageView expandImage;
        @Bind(R.id.todo_list_header_drag) ImageView dragImage;
        @Bind(R.id.header_container) LinearLayout container;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true); // sticky = true
            ButterKnife.bind(this, view);
        }

        @Override
        protected boolean isViewCollapsibleOnLongClick() {
            return false;
        }

        @Override
        public void toggleActivation() {
            super.toggleActivation();
            if (mAdapter.getSelectedItemCount() > 0) {
                container.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                expandImage.setVisibility(View.GONE);
                dragImage.setVisibility(View.VISIBLE);
                mAdapter.collapseAll();
            } else {
                container.setBackgroundColor(getColor(R.color.colorPrimary));
                expandImage.setVisibility(View.VISIBLE);
                dragImage.setVisibility(View.GONE);
            }
        }

        @Override
        protected boolean isViewExpandableOnClick() {
            return mAdapter.getSelectedItemCount() == 0 && !itemView.isActivated();
        }

        @Override
        protected void expandView(int position) {
            if (isViewExpandableOnClick()) {
                super.expandView(position);
                expandImage.setImageResource(R.drawable.ic_expand_less_black_24dp);
            }
        }

        @Override
        protected void collapseView(int position) {
            if (isViewExpandableOnClick()) {
                super.collapseView(position);
                expandImage.setImageResource(R.drawable.ic_expand_more_black_24dp);
            }
        }

        private int getColor(@ColorRes int color) {
            return getFrontView().getResources().getColor(color);
        }

    }

}
