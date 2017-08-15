package com.todo.codepath.codepathtodo.todos;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.todo.codepath.codepathtodo.addedittask.AddEditTaskActivity;
import com.todo.codepath.codepathtodo.data.local.TasksDataSource;
import com.todo.codepath.codepathtodo.data.local.TasksLocalDataSource;
import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link TasksFragment}), retrieves the data and updates the
 * UI as required.
 */
public class TasksPresenter implements TasksContract.Presenter {

    private final TasksLocalDataSource mTaskLocalDataSource;

    private final TasksContract.View mTasksView;

    private boolean mFirstLoad = true;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    public TasksPresenter(@NonNull TasksLocalDataSource tasksLocalDataSource, @NonNull TasksContract.View tasksView) {
        mTaskLocalDataSource = tasksLocalDataSource;
        mTasksView = tasksView;

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mTasksView.showSuccessfullySavedMessage();
        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        // Simplification for sample: a network reload will be forced on first load.
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link //TasksDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            //mTasksRepository.refreshTasks();
        }
        mTaskLocalDataSource.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<TaskTodo> tasks) {
                List<TaskTodo> tasksToShow = new ArrayList<>();

                // The view may not be able to handle UI updates anymore
                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false);
                }

                // We filter the tasks based on the requestType
                for (TaskTodo task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }
                processTasks(tasksToShow);
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTasksView.isActive()) {
                    return;
                }
                mTasksView.showLoadingTasksError();
            }

            @Override
            public void onNoTasksInTable() {
                mTasksView.showNoTasks();
            }
        });
    }

    private void processTasks(List<TaskTodo> tasks) {
        if (tasks.isEmpty()) {
            mTasksView.showNoTasks();
        } else {
            // Show the list of tasks
            mTasksView.showTasks(tasks);
            // Set the filter label's text.
            showFilterLabel();
        }
    }

    @Override
    public void clearCompletedTasks() {
        mTaskLocalDataSource.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public void addNewTask(String taskId) {
        mTasksView.showAddEditTask(taskId);
    }

    @Override
    public void deleteAllTasks() {
        mTaskLocalDataSource.deleteAllTasks();
        mTasksView.showCompletedDeletedAllTasks();
        loadTasks(false, false);
    }

    @Override
    public void completeTask(@NonNull TaskTodo completedTask) {
        mTaskLocalDataSource.completeTask(completedTask);
        mTasksView.showTaskMarkedComplete();
        loadTasks(false, false);
    }

    @Override
    public void activateTask(@NonNull TaskTodo activeTask) {
        mTaskLocalDataSource.activateTask(activeTask);
        mTasksView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void deleteTasks(String taskId) {
        mTaskLocalDataSource.deleteTasks(taskId, new TasksDataSource.DeleteTaskCallback() {
            @Override
            public void onDeletedTaskSuccessful() {
                mTasksView.showSuccessfullyDeletedTaskMessage();
                loadTasks(false, false);
            }

            @Override
            public void onLastRowDeleted() {
                mTasksView.showNoTasks();
            }

            @Override
            public void onDeleteTaskError() {
                mTasksView.showDeleteTasksError();
            }
        });
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ACTIVE_TASKS:
                mTasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTasksView.showCompletedFilterLabel();
                break;
            default:
                mTasksView.showAllFilterLabel();
                break;
        }
    }
}