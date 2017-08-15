package com.todo.codepath.codepathtodo.todos;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.todo.codepath.codepathtodo.R;
import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskListHolder> {
    private List<TaskTodo> mListArrayList;
    private int mItemLayout;
    private RecyclerItemClickListener mRecyclerItemClickListener;

    public TaskListAdapter(ArrayList<TaskTodo> addressBooksArrayList, int itemLayout) {
        this.mListArrayList = addressBooksArrayList;
        this.mItemLayout = itemLayout;
    }

    @Override
    public TaskListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
        return new TaskListHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskListHolder holder, int position) {
        final TaskTodo taskObj = mListArrayList.get(position);
        Context context = holder.itemView.getContext();
        holder.mTitle.setText(taskObj.getTitle());
        holder.mDueDate.setText(context.getResources().getString(R.string.due_date, taskObj.getDueDate()));

        holder.mPriority.setText(taskObj.getPriority());
        if (taskObj.getPriority().equalsIgnoreCase(holder.priority_array[0])) {
            holder.mPriority.setTextColor(Color.rgb(34, 139, 34));
        } else if (taskObj.getPriority().equalsIgnoreCase(holder.priority_array[1])) {
            holder.mPriority.setTextColor(Color.rgb(255, 140, 0));
        } else if (taskObj.getPriority().equalsIgnoreCase(holder.priority_array[2])) {
            holder.mPriority.setTextColor(Color.RED);
        }

        holder.mCheckBox.setChecked(taskObj.isCompleted());

        if (taskObj.isCompleted()) {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.list_completed_touched));
        } else {
            holder.itemView.setBackground(context.getResources().getDrawable(R.drawable.touch_bg));
        }
    }

    @Override
    public void onBindViewHolder(final TaskListHolder taskHolder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(taskHolder, position);
        } else {
            Bundle bundle = (Bundle) payloads.get(0);
            for (String key : bundle.keySet()) {
                switch (key) {
                    case TaskDiffCallback.KEY_TITLE:
                        taskHolder.mTitle.setText(bundle.getString(key));
                        break;
                    case TaskDiffCallback.KEY_DUE_DATE:
                        taskHolder.mDueDate.setText(bundle.getString(key));
                        break;
                    case TaskDiffCallback.KEY_PRIORITY:
                        taskHolder.mPriority.setText(bundle.getString(key));
                        if (bundle.getString(key).equalsIgnoreCase(taskHolder.priority_array[0])) {
                            taskHolder.mPriority.setTextColor(Color.rgb(34, 139, 34));
                        } else if (bundle.getString(key).equalsIgnoreCase(taskHolder.priority_array[1])) {
                            taskHolder.mPriority.setTextColor(Color.rgb(255, 140, 0));
                        } else if (bundle.getString(key).equalsIgnoreCase(taskHolder.priority_array[2])) {
                            taskHolder.mPriority.setTextColor(Color.RED);
                        }

                    case TaskDiffCallback.KEY_COMPLETE:
                        taskHolder.mCheckBox.setChecked(bundle.getBoolean(key));

                        if (bundle.getBoolean(key)) {
                            taskHolder.itemView.setBackground(taskHolder.itemView.getContext().getResources().getDrawable(R.drawable.list_completed_touched));
                        } else {
                            taskHolder.itemView.setBackground(taskHolder.itemView.getContext().getResources().getDrawable(R.drawable.touch_bg));
                        }
                        break;
                }
            }
        }
    }

    public List<TaskTodo> getTaskTodoArrayList() {
        return this.mListArrayList;
    }

    @Override
    public int getItemCount() {
        return mListArrayList.size();
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.mRecyclerItemClickListener = recyclerItemClickListener;
    }

    /**
     * Update address book list items through DiffUtil flow
     *
     * @param newTaskTodoList new address book list
     */
    public void updateTaskTodoListItems(List<TaskTodo> newTaskTodoList) {
        final TaskDiffCallback diffCallback = new TaskDiffCallback(this.mListArrayList, newTaskTodoList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mListArrayList.clear();
        this.mListArrayList.addAll(newTaskTodoList);
        diffResult.dispatchUpdatesTo(this);
    }

    @SuppressWarnings("WeakerAccess")
    public class TaskListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.complete)
        AppCompatCheckBox mCheckBox;

        @BindView(R.id.title)
        AppCompatTextView mTitle;

        @BindView(R.id.due_date)
        AppCompatTextView mDueDate;

        @BindView(R.id.priority)
        AppCompatTextView mPriority;

        @BindView(R.id.task_edit)
        ImageView mTaskEdit;

        String[] priority_array;

        public TaskListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCheckBox.setOnClickListener(this);
            mTaskEdit.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            priority_array = itemView.getContext().getResources().getStringArray(R.array.priority_array);
        }

        @Override
        public void onClick(View view) {
            if (mRecyclerItemClickListener != null)
                mRecyclerItemClickListener.onItemClickListener(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mRecyclerItemClickListener != null) {
                mRecyclerItemClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
            return false;
        }
    }
}