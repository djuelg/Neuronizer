package de.djuelg.neuronizer.presentation.ui;

/**
 * Created by Domi on 22.07.2017.
 */

public class Constants {

    // List of Keys to interact between fragments or activities
    public static final String KEY_UUID = "KEY_UUID";
    public static final String KEY_TODO_LIST_UUID = "KEY_TODO_LIST_UUID";
    public static final String KEY_ITEM_UUID = "KEY_ITEM_UUID";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_INTRO_TYPE = "KEY_INTRO_TYPE";
    public static final String KEY_INTRO_PREVIEW = "KEY_INTRO_PREVIEW";
    public static final String KEY_INTRO_TODO_LIST = "KEY_INTRO_TODO_LIST";

    // ActionBar
    public static final String FONT_NAME = "Quicksand-Regular.ttf";
    public static final String FONT_NAME_FULL = "fonts/Quicksand-Regular.ttf";

    // Flexible Adapter
    public static final int SWIPE_LEFT_TO_EDIT = 4;
    public static final int SWIPE_RIGHT_TO_DELETE = 8;
    public static final boolean STICKY = true;

    // Shared prefs
    public static final String KEY_PREF_SWITCH = "key_pref_switch_database";
    public static final String KEY_PREF_IMPORT = "key_pref_import_database";
    public static final String KEY_PREF_EXPORT = "key_pref_export_database";
    public static final String KEY_PREF_ACTIVE_REPO = "key_pref_active_repo";
    public static final String KEY_PREF_INTRO = "key_pref_introduction";
    public static final String KEY_PREF_ABOUT = "key_pref_show_about";
    public static final String KEY_PREF_TODO = "key_pref_confirm_delete_todo_list";
    public static final String KEY_PREF_HEADER_OR_ITEM = "key_pref_confirm_delete_header_or_item";
    public static final String KEY_PREF_SORTING = "key_pref_sorting";
    public static final String KEY_PREF_PREVIEW_INTRO_SHOWN = "key_pref_preview_intro_shown";
    public static final String KEY_PREF_TODO_LIST_INTRO_SHOWN = "key_pref_todo_intro_shown";

    // RichEditor
    public static final int EDITOR_HEIGHT = 200;
    public static final int EDITOR_PADDING = 8;
    public static final int EDITOR_FONT_SIZE = 18;
    public static final String EDITOR_DETAILS_CSS = "css/Details.css";
    public static final String EDITOR_ABOUT_CSS = "css/About.css";

    // Permissions
    public static final int READ_EXTERNAL_STORAGE = 0;
    public static final int WRITE_EXTERNAL_STORAGE = 1;

}
