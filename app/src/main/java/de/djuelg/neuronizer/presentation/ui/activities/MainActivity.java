package de.djuelg.neuronizer.presentation.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.djuelg.neuronizer.R;
import de.djuelg.neuronizer.domain.executor.impl.ThreadExecutor;
import de.djuelg.neuronizer.domain.interactors.exception.ExceptionId;
import de.djuelg.neuronizer.domain.model.TodoListPreview;
import de.djuelg.neuronizer.presentation.exception.ExceptionHandler;
import de.djuelg.neuronizer.presentation.presenters.MainPresenter;
import de.djuelg.neuronizer.presentation.presenters.MainPresenter.View;
import de.djuelg.neuronizer.presentation.presenters.impl.MainPresenterImpl;
import de.djuelg.neuronizer.storage.TodoListRepositoryImpl;
import de.djuelg.neuronizer.threading.MainThreadImpl;

public class MainActivity extends AppCompatActivity implements View {

    @Bind(R.id.welcome_textview)
    TextView mWelcomeTextView;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // create a presenter for this view
        mPresenter = new MainPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this,
                new TodoListRepositoryImpl()
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        // let's start welcome message retrieval when the app resumes
        mPresenter.resume();
    }

    @Override
    public void showProgress() {
        mWelcomeTextView.setText("Retrieving...");
    }

    @Override
    public void hideProgress() {
        Toast.makeText(this, "Retrieved!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(ExceptionId exceptionId) {
        final String message = ExceptionHandler.getMessage(getResources(), exceptionId);
        mWelcomeTextView.setText(message);
    }

    @Override
    public void displayPreviews(Iterable<TodoListPreview> previews) {
        mWelcomeTextView.setText(previews.iterator().next().getTodoList().getTitle());
    }
}
