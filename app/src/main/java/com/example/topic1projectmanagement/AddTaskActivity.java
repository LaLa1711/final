package com.example.topic1projectmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private EditText taskNameEditText, assigneeEditText, startDateEditText, endDateEditText;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dbHelper = new DBHelper(this);

        taskNameEditText = findViewById(R.id.taskNameEditText);
        assigneeEditText = findViewById(R.id.assigneeEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskName = taskNameEditText.getText().toString().trim();
                String assignee = assigneeEditText.getText().toString().trim();
                String startDate = startDateEditText.getText().toString().trim();
                String endDate = endDateEditText.getText().toString().trim();
                int estimateDays = 0;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                try {
                    Date startDateObj = sdf.parse(startDate);
                    Date endDateObj = sdf.parse(endDate);
                    if (!isValidDate(startDateObj, startDate) || !isValidDate(endDateObj, endDate)) {
                        Toast.makeText(AddTaskActivity.this, "Invalid date values! Please enter correct day, month, and year.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (startDateObj.after(endDateObj)) {
                        Toast.makeText(AddTaskActivity.this, "Start date must be before or equal to End date.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (startDateObj != null && endDateObj != null) {
                        long differenceInMillis = endDateObj.getTime() - startDateObj.getTime();
                        estimateDays = (int) (differenceInMillis / (1000 * 60 * 60 * 24));
                        estimateDays += 1;
                    }
                } catch (ParseException e) {
                    Toast.makeText(AddTaskActivity.this, "Invalid date format! Please use DD/MM/YYYY.", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (taskName.isEmpty() || assignee.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || estimateDays < 0) {
                    Toast.makeText(AddTaskActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Task newTask = new Task(0, taskName, assignee, startDate, endDate, estimateDays);

                try {
                    dbHelper.addTask(newTask);
                    Toast.makeText(AddTaskActivity.this, "Task added successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } catch (ParseException e) {
                    Toast.makeText(AddTaskActivity.this, "Error adding task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isValidDate(Date dateObj, String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateObj);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (month < 1 || month > 12) {
            return false;
        }

        if (month == 2) {
            boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
            if (isLeapYear && day > 29) {
                return false;
            } else if (!isLeapYear && day > 28) {
                return false;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day > 30) {
                return false;
            }
        } else {
            if (day > 31) {
                return false;
            }
        }

        return true;
    }
}
