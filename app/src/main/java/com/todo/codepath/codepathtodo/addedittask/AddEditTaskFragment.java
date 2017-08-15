package com.todo.codepath.codepathtodo.addedittask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.todo.codepath.codepathtodo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskContract.Presenter mPresenter;

    @BindView(R.id.add_task_title)
    TextView mTitle;

    @BindView(R.id.add_task_description)
    TextView mDescription;

    @BindView(R.id.add_task_due_date)
    TextView mTvDueDate;

    @BindView(R.id.add_task_priority)
    AppCompatSpinner mSpinnerPriority;

    @BindView(R.id.fab_edit_task_done)
    FloatingActionButton mFab;

    ArrayAdapter<CharSequence> mAdapter;

    private String priority;

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    public AddEditTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull AddEditTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFab.setImageResource(R.drawable.ic_done);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString(), mTvDueDate.getText().toString(), priority);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create an ArrayAdapter using the string array and a default spinner layout
        mAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priority = mAdapter.getItem(0).toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addtask_frag, container, false);
        ButterKnife.bind(this, root);
        // Apply the mAdapter to the spinner
        mSpinnerPriority.setAdapter(mAdapter);
        mTvDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        mSpinnerPriority.setOnItemSelectedListener(getItemSelect());
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setDescription(String description) {
        mDescription.setText(description);
    }

    @Override
    public void setDueDate(String dueDate) {
        mTvDueDate.setText(dueDate);
    }

    @Override
    public void setPriority(String priority) {
        mSpinnerPriority.setSelection(mAdapter.getPosition(priority));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
            String formattedDate = sdf.format(c.getTime());
            mTvDueDate.setText(formattedDate);
        }
    };

    private AdapterView.OnItemSelectedListener getItemSelect() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                priority = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }
}
