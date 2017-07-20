package de.djuelg.neuronizer.presentation.ui.flexibleAdapter;

import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by djuelg on 20.07.17.
 */

public class TodoListPreviewUI extends AbstractFlexibleItem<TodoListPreviewUI.ViewHolder> {

    private final TodoListPreview preview;

    public TodoListPreviewUI(TodoListPreview preview) {
        this.preview = Objects.requireNonNull(preview);
    }

    @Override
    public boolean isSwipeable() {
        return true;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.todo_list_preview;
    }

    @Override
    public ViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new ViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        DateFormat sdf = SimpleDateFormat.getDateInstance();
        // TODO Test header and items
        String header = (preview.getHeader() != null)
                ? preview.getHeader().getTitle()
                : holder.getContentView().getResources().getString(R.string.missing_header_preview);
        String items = (preview.getItems() != null)
                ? formatItems(preview.getItems())
                : "";

        holder.title.setText(preview.getTodoList().getTitle());
        holder.lastChange.setText(sdf.format(preview.getTodoList().getChangedAt()));
        holder.header.setText(header);
        holder.items.setText(items);
    }

    private String formatItems(Iterable<TodoListItem> items) {
        String formattedItems = "";
        for (TodoListItem item : items) {
            formattedItems += item.getTitle() + ", ";
        }

        return formattedItems.substring(0, formattedItems.length() -2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TodoListPreviewUI that = (TodoListPreviewUI) o;
        return Objects.equals(preview, that.preview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preview);
    }

    /**
     * Needed ViewHolder
     */
    class ViewHolder extends FlexibleViewHolder {

        @Bind(R.id.front_view) View frontView;
        @Bind(R.id.rear_left_view) View rearLeftView;
        @Bind(R.id.rear_right_view) View rearRightView;
        @Bind(R.id.title_preview) TextView title;
        @Bind(R.id.last_change_preview) TextView lastChange;
        @Bind(R.id.header_preview) TextView header;
        @Bind(R.id.items_preview) TextView items;

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @Override
        public float getActivationElevation() {
            return 8f;
        }

        @Override
        public View getFrontView() {
            return frontView;
        }

        @Override
        public View getRearLeftView() {
            return rearLeftView;
        }

        @Override
        public View getRearRightView() {
            return rearRightView;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ViewHolder that = (ViewHolder) o;
            return Objects.equals(title, that.title) &&
                    Objects.equals(lastChange, that.lastChange) &&
                    Objects.equals(header, that.header) &&
                    Objects.equals(items, that.items);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, lastChange, header, items);
        }
    }
}
