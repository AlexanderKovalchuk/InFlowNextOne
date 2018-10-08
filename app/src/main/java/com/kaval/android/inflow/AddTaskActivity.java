package com.kaval.android.inflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;
import com.kaval.android.inflow.utils.TaskBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

@SuppressWarnings("TooBroadScope")
public class AddTaskActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    public static final String NEW_TASK = "new_task";

    @BindView(R.id.add_task_name)
    TextInputLayout nameTV;

    @BindView(R.id.add_task_description)
    TextInputLayout descriptionTV;

    @BindView(R.id.add_task_duration)
    TextInputLayout durationTV;

    @BindView(R.id.add_task_datetime)
    TextView dueDateTV;

    Date dateRepresentation;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);
        nameTV.getEditText().addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                //method1()
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
                //method2()
            }
            public void onTextChanged(CharSequence s, int start, int before, int count){
                if (menu != null) {
                    menu.findItem(R.id.menu_done).setVisible(!nameTV.getEditText().getText().toString().isEmpty());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case android.R.id.home:
//                setKeyboardVisible(false);
//                onBackPressed();
//                return true;
            case R.id.menu_done:
                hideKeyboard(this);
                createAndSendResult();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAndSendResult() {
        TaskBuilder builder = new TaskBuilder()
                .setName(nameTV.getEditText().getText().toString())
                .setDescription(descriptionTV.getEditText().getText().toString())
                .setDuration(!durationTV.getEditText().getText().toString().isEmpty()?Long.parseLong(durationTV.getEditText().getText().toString()):-1)
                .setDueDate(dateRepresentation)
                .setState(TaskState.TODO);
        Task task = builder.createTask();
        task.save();
        finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirmation_step, menu);
        this.menu = menu;
        return true;
    }

    @OnFocusChange(R.id.add_task_datetime)
    void onAddContactClicked() {
        if (dueDateTV.hasFocus()) {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    AddTaskActivity.this,
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");
        }
    }

    int year;
    int month;
    int day;

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;

        TimePickerDialog dpd = TimePickerDialog.newInstance(
                AddTaskActivity.this,
                0, // Initial year selection
                0, // Initial month selection
                true // Inital day selection
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        dateRepresentation = cal.getTime();
        dueDateTV.setText(dateRepresentation.toString());
    }
}
