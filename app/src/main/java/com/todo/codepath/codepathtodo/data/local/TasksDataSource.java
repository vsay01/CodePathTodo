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

import android.support.annotation.NonNull;

import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import java.util.List;

/**
 * Main entry point for accessing tasks data.
 * <p>
 * For simplicity, only getTasks() and getTask() have callbacks. Consider adding callbacks to other
 * methods to inform the user of network/database errors or successful operations.
 * For example, when a new task is created, it's synchronously stored in cache but usually every
 * operation on database or network should be executed in a different thread.
 */
public interface TasksDataSource {

    interface LoadTasksCallback {

        void onTasksLoaded(List<TaskTodo> tasks);

        void onNoTasksInTable();

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(TaskTodo task);

        void onNoTasksInTable();

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallback callback);

    void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback);

    void saveTask(@NonNull TaskTodo task);

    void completeTask(@NonNull TaskTodo task);

    void activateTask(@NonNull TaskTodo task);

    interface DeleteTaskCallback {

        void onDeletedTaskSuccessful();

        void onLastRowDeleted();

        void onDeleteTaskError();
    }

    void deleteTasks(String taskId, @NonNull DeleteTaskCallback callback);

    void deleteAllTasks();

    void clearCompletedTasks();
}