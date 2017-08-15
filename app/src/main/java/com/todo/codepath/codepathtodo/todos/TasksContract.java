package com.todo.codepath.codepathtodo.todos;

import android.support.annotation.NonNull;

import com.todo.codepath.codepathtodo.BasePresenter;
import com.todo.codepath.codepathtodo.BaseView;
import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface TasksContract {

    interface View extends BaseView<Presenter> {

        void showTasks(List<TaskTodo> tasks);

        void showAddEditTask(String taskId);

        void showLoadingTasksError();

        void showDeleteTasksError();

        void showNoTasks();

        void showNoActiveTasks();

        void showNoCompletedTasks();

        void showSuccessfullySavedMessage();

        void showSuccessfullyDeletedTaskMessage();

        boolean isActive();

        void showFilteringPopUpMenu();

        void setLoadingIndicator(boolean b);

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        void showCompletedTasksCleared();

        void showCompletedDeletedAllTasks();

        void showActiveFilterLabel();

        void showCompletedFilterLabel();

        void showAllFilterLabel();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTasks(boolean forceUpdate);

        void addNewTask(String taskId);

        void deleteAllTasks();

        void completeTask(@NonNull TaskTodo completedTask);

        void activateTask(@NonNull TaskTodo activeTask);

        void deleteTasks(String taskId);

        void clearCompletedTasks();

        void setFiltering(TasksFilterType requestType);
    }
}