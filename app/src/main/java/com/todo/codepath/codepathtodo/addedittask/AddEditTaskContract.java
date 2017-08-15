package com.todo.codepath.codepathtodo.addedittask;


import com.todo.codepath.codepathtodo.BasePresenter;
import com.todo.codepath.codepathtodo.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {

        void showEmptyTaskError();

        void showTasksList();

        void setTitle(String title);

        void setDescription(String description);

        void setDueDate(String dueDate);

        void setPriority(String priority);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void saveTask(String title, String description, String dueDate, String priority);

        void populateTask();

        boolean isDataMissing();
    }
}