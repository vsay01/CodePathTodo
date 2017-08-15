package com.todo.codepath.codepathtodo.todos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.todo.codepath.codepathtodo.R;
import com.todo.codepath.codepathtodo.addedittask.AddEditTaskActivity;
import com.todo.codepath.codepathtodo.addedittask.AddEditTaskFragment;
import com.todo.codepath.codepathtodo.data.model.TaskTodo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Display a grid of {@link TaskTodo}s. User can choose to view all, active or completed tasks.
 */
public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter;

    private TaskListAdapter mListAdapter;

    @BindView(R.id.noTasksIcon)
    ImageView mNoTaskIcon;

    @BindView(R.id.noTasksMain)
    TextView mNoTaskMainView;

    @BindView(R.id.noTasksAdd)
    TextView mNoTaskAddView;

    @BindView(R.id.noTasks)
    View mNoTasksView;

    @BindView(R.id.tasksLL)
    LinearLayout mTasksView;

    @BindView(R.id.filteringLabel)
    TextView mFilteringLabelView;

    @BindView(R.id.tasks_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab_add_task)
    FloatingActionButton mFab;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar mProgressBar;

    private LinearLayoutManager mLayoutManager;
    private ArrayList<TaskTodo> myDataSet = new ArrayList<>();

    public TasksFragment() {
        // Requires empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new TaskListAdapter(myDataSet, R.layout.task_item);
        mListAdapter.setRecyclerItemClickListener(mTaskRecycleItemClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tasks_frag, container, false);
        ButterKnife.bind(this, root);

        initView();

        return root;
    }

    private void initView() {
        mNoTaskAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddEditTask(null);
            }
        });

        mFab.setImageResource(R.drawable.ic_add);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewTask(null);
            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // add divider
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getActivity(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setAdapter(mListAdapter);
        setHasOptionsMenu(true);
    }

    @Override
    public void showTasks(List<TaskTodo> tasks) {
        mListAdapter.updateTaskTodoListItems(tasks);
        mTasksView.setVisibility(View.VISIBLE);
        mNoTasksView.setVisibility(View.GONE);
        setLoadingIndicator(false);
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        mTasksView.setVisibility(View.GONE);
        mNoTasksView.setVisibility(View.VISIBLE);

        mNoTaskMainView.setText(mainText);
        mNoTaskIcon.setImageDrawable(getResources().getDrawable(iconRes));
        mNoTaskAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
        setLoadingIndicator(false);
    }

    @Override
    public void showAddEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showActiveFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        mFilteringLabelView.setText(getResources().getString(R.string.label_all));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showDeleteTasksError() {
        showMessage(getString(R.string.delete_tasks_error));
    }

    @Override
    public void showNoActiveTasks() {

    }

    @Override
    public void showNoCompletedTasks() {

    }

    @Override
    public void showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public void showSuccessfullyDeletedTaskMessage() {
        showMessage(getString(R.string.successfully_delete_task_message));
    }

    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showCompletedDeletedAllTasks() {
        showMessage(getString(R.string.successfully_all_delete_task_message));
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void setLoadingIndicator(boolean b) {
        if (b) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Listener for clicks on icon more of the address book item in the RecyclerView.
     */
    private RecyclerItemClickListener mTaskRecycleItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            switch (view.getId()) {
                case R.id.complete:
                    if (!mListAdapter.getTaskTodoArrayList().get(position).isCompleted()) {
                        mPresenter.completeTask(mListAdapter.getTaskTodoArrayList().get(position));
                    } else {
                        mPresenter.activateTask(mListAdapter.getTaskTodoArrayList().get(position));
                    }
                    break;
                case R.id.task_edit:
                    showAddEditTask(mListAdapter.getTaskTodoArrayList().get(position).getId());
                    break;
                default:
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    TaskDetailDialogFragment taskDetailDialogFragment = TaskDetailDialogFragment.newInstance(mListAdapter.getTaskTodoArrayList().get(position));
                    taskDetailDialogFragment.show(fm, "fragment_edit_name");
                    break;
            }
        }

        @Override
        public void onItemLongClick(View view, int position) {
            mPresenter.deleteTasks(mListAdapter.getTaskTodoArrayList().get(position).getId());
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.clearCompletedTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(true);
                break;
            case R.id.menu_delete:
                mPresenter.deleteAllTasks();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }
}