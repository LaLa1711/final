package com.example.topic1projectmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTaskActivity extends AppCompatActivity {
    private EditText taskNameEditText, assigneeEditText, startDateEditText, endDateEditText;
    private Button saveButton;
    private DBHelper dbHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        dbHelper = new DBHelper(this);
        taskId = getIntent().getIntExtra("taskId", -1);

        taskNameEditText = findViewById(R.id.taskNameEditText);
        assigneeEditText = findViewById(R.id.assigneeEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        saveButton = findViewById(R.id.saveButton);

        Task task = dbHelper.getTaskById(taskId);
        if (task != null) {
            taskNameEditText.setText(task.getName());
            assigneeEditText.setText(task.getAssigneeName());
            startDateEditText.setText(task.getStartDate());
            endDateEditText.setText(task.getEndDate());

        }

        // Xử lý sự kiện khi nhấn nút Lưu
        saveButton.setOnClickListener(v -> {
            String name = taskNameEditText.getText().toString();
            String assignee = assigneeEditText.getText().toString();
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();

            int estimateDays = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);

            try {
                Date startDateObj = sdf.parse(startDate);
                Date endDateObj = sdf.parse(endDate);

                if (!isValidDate(startDateObj, startDate) || !isValidDate(endDateObj, endDate)) {
                    Toast.makeText(EditTaskActivity.this, "Invalid date values! Please enter correct day, month, and year.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (startDateObj.after(endDateObj)) {
                    Toast.makeText(EditTaskActivity.this, "Start date must be before or equal to End date.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (startDateObj != null && endDateObj != null) {
                    long differenceInMillis = endDateObj.getTime() - startDateObj.getTime();
                    estimateDays = (int) (differenceInMillis / (1000 * 60 * 60 * 24));
                    estimateDays += 1;
                }
            } catch (ParseException e) {
                Toast.makeText(EditTaskActivity.this, "Invalid date format! Please use dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
                return;
            }

            Task taskToUpdate = new Task(taskId, name, assignee, startDate, endDate, estimateDays);


            try {
                dbHelper.updateTask(taskToUpdate);
                Toast.makeText(EditTaskActivity.this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } catch (ParseException e) {
                Toast.makeText(EditTaskActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
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
