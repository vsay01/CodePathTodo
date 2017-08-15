package com.todo.codepath.codepathtodo.todos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import java.util.List;

public class TaskDiffCallback extends DiffUtil.Callback {
    private final List<TaskTodo> mOldTaskList;
    private final List<TaskTodo> mNewTaskList;
    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_DUE_DATE = "DUE DATE";
    public static final String KEY_PRIORITY = "PRIORITY";
    public static final String KEY_COMPLETE = "COMPLETE";

    public TaskDiffCallback(List<TaskTodo> oldAddressBookList, List<TaskTodo> newAddressBookList) {
        this.mOldTaskList = oldAddressBookList;
        this.mNewTaskList = newAddressBookList;
    }

    @Override
    public int getOldListSize() {
        return (mOldTaskList == null) ? 0 : mOldTaskList.size();
    }

    @Override
    public int getNewListSize() {
        return (mNewTaskList == null) ? 0 : mNewTaskList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return (mOldTaskList.get(oldItemPosition).getId().equals(mNewTaskList.get(newItemPosition).getId()));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final TaskTodo oldTask = mOldTaskList.get(oldItemPosition);
        final TaskTodo newTask = mNewTaskList.get(newItemPosition);

        return oldTask.getId().equals(newTask.getId())
                && oldTask.getTitle().equals(newTask.getTitle())
                && oldTask.getDueDate().equals(newTask.getDueDate())
                && oldTask.getPriority().equals(newTask.getPriority())
                && oldTask.isCompleted() == newTask.isCompleted();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        TaskTodo newTask = mNewTaskList.get(newItemPosition);
        TaskTodo oldTask = mOldTaskList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newTask.getTitle().equals(oldTask.getTitle())) {
            diffBundle.putString(KEY_TITLE, newTask.getTitle());
        }
        if (!newTask.getDueDate().equals(oldTask.getDueDate())) {
            diffBundle.putString(KEY_DUE_DATE, newTask.getDueDate());
        }
        if (!newTask.getPriority().equals(oldTask.getPriority())) {
            diffBundle.putString(KEY_PRIORITY, newTask.getPriority());
        }
        if (newTask.isCompleted() == oldTask.isCompleted()) {
            diffBundle.putBoolean(KEY_COMPLETE, newTask.isCompleted());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}