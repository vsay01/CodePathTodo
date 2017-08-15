package com.todo.codepath.codepathtodo.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.todo.codepath.codepathtodo.data.local.TasksDataSource;
import com.todo.codepath.codepathtodo.data.local.TasksLocalDataSource;
import com.todo.codepath.codepathtodo.data.model.TaskTodo;

/**
 * Listens to user actions from the UI ({@link AddEditTaskFragment}), retrieves the data and updates
 * the UI as required.
 */
public class AddEditTaskPresenter implements AddEditTaskContract.Presenter,
        TasksDataSource.GetTaskCallback {

    @NonNull
    private final TasksLocalDataSource mTasksLocalDataSource;

    @NonNull
    private final AddEditTaskContract.View mAddTaskView;

    @Nullable
    private String mTaskId;

    private boolean mIsDataMissing;

    /**
     * Creates a presenter for the add/edit view.
     *
     * @param taskId ID of the task to edit or null for a new task
     * @param tasksLocalDataSource a repository of data for tasks
     * @param addTaskView the add/edit view
     * @param shouldLoadDataFromRepo whether data needs to be loaded or not (for config changes)
     */
    public AddEditTaskPresenter(@Nullable String taskId, @NonNull TasksLocalDataSource tasksLocalDataSource,
                                @NonNull AddEditTaskContract.View addTaskView, boolean shouldLoadDataFromRepo) {
        mTaskId = taskId;
        mIsDataMissing = shouldLoadDataFromRepo;

        mTasksLocalDataSource = tasksLocalDataSource;
        mAddTaskView = addTaskView;
        mAddTaskView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask() && mIsDataMissing) {
            populateTask();
        }
    }

    @Override
    public void saveTask(String title, String description, String dueDate, String priority) {
        if (isNewTask()) {
            createTask(title, description, dueDate, priority);
        } else {
            updateTask(title, description, dueDate, priority);
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        mTasksLocalDataSource.getTask(mTaskId, this);
    }

    @Override
    public void onTaskLoaded(TaskTodo task) {
        // The view may not be able to handle UI updates anymore
        if (mAddTaskView.isActive()) {
            mAddTaskView.setTitle(task.getTitle());
            mAddTaskView.setDescription(task.getDescription());
            mAddTaskView.setDueDate(task.getDueDate());
            mAddTaskView.setPriority(task.getPriority());
        }
        mIsDataMissing = false;
    }

    @Override
    public void onNoTasksInTable() {

    }

    @Override
    public void onDataNotAvailable() {
        // The view may not be able to handle UI updates anymore
        if (mAddTaskView.isActive()) {
            mAddTaskView.showEmptyTaskError();
        }
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    private boolean isNewTask() {
        return mTaskId == null;
    }

    private void createTask(String title, String description, String dueDate, String priority) {
        TaskTodo newTask = new TaskTodo(title, description, dueDate, priority);
        if (newTask.isEmpty()) {
            mAddTaskView.showEmptyTaskError();
        } else {
            mTasksLocalDataSource.saveTask(newTask);
            mAddTaskView.showTasksList();
        }
    }

    private void updateTask(String title, String description, String dueDate, String priority) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mTasksLocalDataSource.saveTask(new TaskTodo(title, description, mTaskId, dueDate, priority));
        mAddTaskView.showTasksList(); // After an edit, go back to the list.
    }
}
