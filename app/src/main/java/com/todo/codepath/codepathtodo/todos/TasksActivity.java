package com.todo.codepath.codepathtodo.todos;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.todo.codepath.codepathtodo.R;
import com.todo.codepath.codepathtodo.data.local.TasksLocalDataSource;

import butterknife.ButterKnife;

public class TasksActivity extends AppCompatActivity {

    private TasksPresenter mTasksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_act);
        ButterKnife.bind(this);

        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = TasksFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, tasksFragment);
            transaction.commit();
        }

        // Create the presenter
        mTasksPresenter = new TasksPresenter(TasksLocalDataSource.getInstance(this), tasksFragment);
    }
}