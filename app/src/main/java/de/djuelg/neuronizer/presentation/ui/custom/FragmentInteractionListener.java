package de.djuelg.neuronizer.presentation.ui.custom;

/**
 * This interface must be implemented by activities that contain a
 * fragment to allow an interaction in the fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 */
public interface FragmentInteractionListener {

    // called from PreviewFragment
    void onTodoListSelected(String uuid, String title);

    // called from TodoListFragment

    void onAddItem(String todoListUuid);

    void onMarkdownHelpSelected();
}
