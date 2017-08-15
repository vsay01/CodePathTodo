package com.todo.codepath.codepathtodo.todos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todo.codepath.codepathtodo.R;
import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskDetailDialogFragment extends DialogFragment {

    @BindView(R.id.task_title)
    TextView mTaskTitle;

    @BindView(R.id.task_due_date)
    TextView mTaskDueDate;

    @BindView(R.id.task_priority)
    TextView mTaskPriority;

    @BindView(R.id.task_description)
    TextView mTaskDescription;

    @BindView(R.id.task_complete)
    TextView mTaskComplete;

    public TaskDetailDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    public static TaskDetailDialogFragment newInstance(TaskTodo taskTodo) {
        TaskDetailDialogFragment frag = new TaskDetailDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("task", taskTodo);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_detail, container);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Fetch arguments from bundle and set title
        TaskTodo taskTodo = getArguments().getParcelable("task");
        mTaskTitle.setText(getString(R.string.task_title, taskTodo.getTitle()));
        mTaskDueDate.setText(getString(R.string.task_due_date, taskTodo.getDueDate()));
        mTaskPriority.setText(getString(R.string.task_priority, taskTodo.getPriority()));
        mTaskDescription.setText(getString(R.string.task_description, taskTodo.getDescription()));
        if (taskTodo.isCompleted()) {
            mTaskComplete.setText(getString(R.string.task_complete, "Yes"));
        } else {
            mTaskComplete.setText(getString(R.string.task_complete, "No"));
        }
        getDialog().setTitle(R.string.task_detail_title);
    }

    @OnClick(R.id.img_cancel)
    void dismissDialog() {
        dismiss();
    }
}
