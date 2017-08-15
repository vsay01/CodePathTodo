package com.todo.codepath.codepathtodo.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.todo.codepath.codepathtodo.R;
import com.todo.codepath.codepathtodo.data.local.TasksLocalDataSource;

/**
 * Displays an add or edit task screen.
 */
public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddEditTaskPresenter mAddEditTaskPresenter;

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtask_act);

        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        String taskId = getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        setToolbarTitle(taskId);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance();

            if (getIntent().hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
                addEditTaskFragment.setArguments(bundle);
            }

            addEditTaskFragment = AddEditTaskFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, addEditTaskFragment);
            transaction.commit();
        }

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        // Create the presenter
        mAddEditTaskPresenter = new AddEditTaskPresenter(
                taskId,
                TasksLocalDataSource.getInstance(this),
                addEditTaskFragment,
                shouldLoadDataFromRepo);
    }

    private void setToolbarTitle(@Nullable String taskId) {
        if (taskId == null) {
            mActionBar.setTitle(R.string.add_task);
        } else {
            mActionBar.setTitle(R.string.edit_task);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, mAddEditTaskPresenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}