package de.djuelg.neuronizer.presentation.ui.custom;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.github.paolorotolo.appintro.model.SliderPage;

import java.util.ArrayList;
import java.util.List;

import de.djuelg.neuronizer.R;

import static de.djuelg.neuronizer.presentation.ui.Constants.FONT_NAME_FULL;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_PREVIEW;
import static de.djuelg.neuronizer.presentation.ui.Constants.KEY_TODO_LIST;

/**
 * Created by djuelg on 13.09.17.
 *
 */

public class SliderPageFactory {

    private final Context context;

    public SliderPageFactory(Context context) {
        this.context = context;
    }

    public List<SliderPage> createSlidesFromType(String type) {
        switch (type) {
            case KEY_PREVIEW:
                return createPreviewSlides();
            case KEY_TODO_LIST:
                return createTodoListSlides();
            default:
                throw new IllegalArgumentException(String.format("Type %s not supported", type));
        }
    }

    private List<SliderPage> createPreviewSlides() {
        List<SliderPage> pages = new ArrayList<>(6);
        pages.add(neuronizerIntroPage());
        pages.add(addTodoListPage());
        pages.add(addNotePage());
        pages.add(swipeLeftPage());
        pages.add(swipeRightPage());
        pages.add(sortPreviewsPage());
        return pages;
    }

    private List<SliderPage> createTodoListSlides() {
        List<SliderPage> pages = new ArrayList<>(6);
        pages.add(addCategoryPage());
        pages.add(addThoughtPage());
        pages.add(checkItems());
        pages.add(addWidgetsPage());
        pages.add(dragDropPage());
        pages.add(editCategory());
        return pages;
    }

    // Preview Pages

    private SliderPage neuronizerIntroPage() {
        return createPageWith(R.string.welcome, R.mipmap.neuronizer, R.string.welcome_descr);
    }

    private SliderPage addTodoListPage() {
        return createPageWith(R.string.add_todo_lists, R.drawable.ic_playlist_add_128dp, R.string.add_todo_lists_descr);
    }

    private SliderPage addNotePage() {
        return createPageWith(R.string.add_notes, R.drawable.ic_note_add_128dp, R.string.add_notes_descr);
    }

    private SliderPage addWidgetsPage() {
        return createPageWith(R.string.add_widgets, R.mipmap.widget_preview, R.string.add_widgets_descr);
    }

    private SliderPage sortPreviewsPage() {
        return createPageWith(R.string.sort, R.drawable.ic_sort_128dp, R.string.sort_descr);
    }

    // TodoList pages

    private SliderPage addCategoryPage() {
        return createPageWith(R.string.categorization, R.drawable.ic_create_new_folder_128dp, R.string.add_category_descr);
    }

    private SliderPage addThoughtPage() {
        return createPageWith(R.string.add_items, R.drawable.ic_lightbulb_128dp, R.string.add_item_descr);
    }

    private SliderPage dragDropPage() {
        return createPageWith(R.string.drag_drop, R.drawable.ic_reorder_128dp, R.string.drag_drop_descr);
    }

    private SliderPage editCategory() {
        return createPageWith(R.string.edit_categories, R.drawable.ic_edit_128dp, R.string.edit_category_descr);
    }

    private SliderPage checkItems() {
        return createPageWith(R.string.check_items, R.drawable.ic_done_128dp, R.string.check_items_descr);
    }

    // Pages for all

    private SliderPage swipeLeftPage() {
        return createPageWith(R.string.swipe_left, R.drawable.ic_undo_128dp, R.string.swipe_left_descr);
    }

    private SliderPage swipeRightPage() {
        return createPageWith(R.string.swipe_right, R.drawable.ic_redo_128dp, R.string.swipe_right_descr);
    }

    private SliderPage createPageWith(@StringRes int title, @DrawableRes int drawable, @StringRes int description) {
        SliderPage page = new SliderPage();
        page.setTitle(context.getResources().getString(title));
        page.setImageDrawable(drawable);
        page.setDescription(context.getResources().getString(description));

        page.setBgColor(context.getResources().getColor(R.color.colorPrimary));
        page.setTitleTypeface(FONT_NAME_FULL);
        return page;
    }
}
