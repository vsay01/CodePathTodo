/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.todo.codepath.codepathtodo.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.todo.codepath.codepathtodo.data.model.TaskTodo;
import com.todo.codepath.codepathtodo.data.local.TasksPersistenceContract.TaskEntry;
import java.util.ArrayList;
import java.util.List;

import static android.database.DatabaseUtils.queryNumEntries;

/**
 * Concrete implementation of a data source as a db.
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static TasksLocalDataSource INSTANCE;

    private TasksDbHelper mDbHelper;

    // Prevent direct instantiation.
    private TasksLocalDataSource(@NonNull Context context) {
        mDbHelper = new TasksDbHelper(context);
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context);
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadTasksCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        List<TaskTodo> tasks = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskEntry.COLUMN_NAME_PRIORITY
        };

        Cursor c = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

        int num_row = c.getCount();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                String dueDate =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DUE_DATE));
                String priority =
                        c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PRIORITY));

                TaskTodo task = new TaskTodo(title, description, itemId, completed, dueDate, priority);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if(num_row == 0){
            callback.onNoTasksInTable();
        } else if (tasks.isEmpty()) {
            // This will be called if the table is new or just empty.
            callback.onDataNotAvailable();
        } else {
            callback.onTasksLoaded(tasks);
        }

    }

    @Override
    public void clearCompletedTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] selectionArgs = { "1" };

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    /**
     * Note: {@link GetTaskCallback#onDataNotAvailable()} is fired if the {@link TaskTodo} isn't
     * found.
     */
    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED,
                TaskEntry.COLUMN_NAME_DUE_DATE,
                TaskEntry.COLUMN_NAME_PRIORITY
        };

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { taskId };

        Cursor c = db.query(
                TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        TaskTodo task = null;

        int num_row = c.getCount();

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
            String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
            String description =
                    c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed =
                    c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
            String dueDate =
                    c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_DUE_DATE));

            String priority =
                    c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_PRIORITY));

            task = new TaskTodo(title, description, itemId, completed, dueDate, priority);
        }
        if (c != null) {
            c.close();
        }

        db.close();

        if(num_row == 0){
            callback.onNoTasksInTable();
        } else if (task != null) {
            callback.onTaskLoaded(task);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveTask(@NonNull TaskTodo task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());
        values.put(TaskEntry.COLUMN_NAME_DUE_DATE, task.getDueDate());
        values.put(TaskEntry.COLUMN_NAME_PRIORITY, task.getPriority());

        db.insertWithOnConflict(TaskEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    @Override
    public void completeTask(@NonNull TaskTodo task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { task.getId() };

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void activateTask(@NonNull TaskTodo task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { task.getId() };

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }

    @Override
    public void deleteTasks(String taskId, @NonNull DeleteTaskCallback callback) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " = ?";
        String[] selectionArgs = { taskId };

        int value = db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();

        SQLiteDatabase db_read = mDbHelper.getReadableDatabase();
        long num_row = DatabaseUtils.queryNumEntries(db_read, TaskEntry.TABLE_NAME);

        db_read.close();

        if(num_row == 0){
            callback.onLastRowDeleted();
        }

        if(value == 0){
            callback.onDeleteTaskError();
        }

        callback.onDeletedTaskSuccessful();
    }

    @Override
    public void deleteAllTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();
    }
}