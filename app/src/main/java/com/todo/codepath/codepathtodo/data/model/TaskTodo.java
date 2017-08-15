package com.todo.codepath.codepathtodo.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Immutable model class for a TaskTodo.
 */
public final class TaskTodo implements Parcelable {

    @NonNull
    private final String mId;

    @Nullable
    private final String mTitle;

    @Nullable
    private final String mDescription;

    private final String mDueDate;

    private final String mPriority;

    private final boolean mCompleted;

    /**
     * Use this constructor to create a new active TaskTodo.
     *
     * @param title       title of the task
     * @param description description of the task
     */
    public TaskTodo(@Nullable String title, @Nullable String description, String dueDate, String priority) {
        this(title, description, UUID.randomUUID().toString(), false, dueDate, priority);
    }

    /**
     * Use this constructor to create an active TaskTodo if the TaskTodo already has an id (copy of another
     * TaskTodo).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     */
    public TaskTodo(@Nullable String title, @Nullable String description, @NonNull String id, String dueDate, String priority) {
        this(title, description, id, false, dueDate, priority);
    }

    /**
     * Use this constructor to specify a completed TaskTodo if the TaskTodo already has an id (copy of
     * another TaskTodo).
     *
     * @param title       title of the task
     * @param description description of the task
     * @param id          id of the task
     * @param completed   true if the task is completed, false if it's active
     */
    public TaskTodo(@Nullable String title, @Nullable String description,
                    @NonNull String id, boolean completed, String dueDate, String priority) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mCompleted = completed;
        mDueDate = dueDate;
        mPriority = priority;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public boolean isActive() {
        return !mCompleted;
    }

    public String getDueDate() {
        return mDueDate;
    }

    public String getPriority() {
        return mPriority;
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(mTitle) &&
                TextUtils.isEmpty(mDescription);
    }

    @Override
    public String toString() {
        return "TaskTodo with title " + mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mDueDate);
        parcel.writeString(mPriority);
        parcel.writeString(mDescription);
        parcel.writeByte((byte) (mCompleted ? 1 : 0));     //if mCompleted == true, byte == 1
    }

    public static final Parcelable.Creator<TaskTodo> CREATOR
            = new Parcelable.Creator<TaskTodo>() {
        public TaskTodo createFromParcel(Parcel in) {
            return new TaskTodo(in);
        }

        public TaskTodo[] newArray(int size) {
            return new TaskTodo[size];
        }
    };

    private TaskTodo(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDueDate = in.readString();
        mDescription = in.readString();
        mPriority = in.readString();
        mCompleted = in.readByte() != 0;
    }
}