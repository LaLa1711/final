package com.example.topic1projectmanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GanttChartActivity extends AppCompatActivity {

    private EditText startDateEditText;
    private EditText endDateEditText;
    private GridLayout dateGridLayout;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gantt_chart);

        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        dateGridLayout = findViewById(R.id.dateGridLayout);
        recyclerView = findViewById(R.id.recyclerView);
        Button showDatesButton = findViewById(R.id.showDatesButton);

        dbHelper = new DBHelper(this);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, dbHelper, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        showDatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateGrid();
            }
        });
    }

    private void showDateGrid() {
        String startDateStr = startDateEditText.getText().toString();
        String endDateStr = endDateEditText.getText().toString();

        if (TextUtils.isEmpty(startDateStr) || TextUtils.isEmpty(endDateStr)) {
            Toast.makeText(this, "Please enter both dates", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            dateGridLayout.removeAllViews();
            addDateHeaders(startDate, endDate);

            loadTasksWithinDateRange(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format. Use dd/MM/yyyy", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDateHeaders(Date startDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            TextView dateTextView = new TextView(this);
            dateTextView.setText(new SimpleDateFormat("dd/MM").format(calendar.getTime()));
            dateTextView.setLayoutParams(new GridLayout.LayoutParams());
            dateTextView.setPadding(8, 8, 8, 8);
            dateTextView.setTextColor(Color.parseColor("#000000"));
            dateGridLayout.addView(dateTextView);

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void loadTasksWithinDateRange(Date startDate, Date endDate) {
        taskList.clear();
        List<Task> tasks = dbHelper.getTasksWithinDateRange(startDate, endDate);
        taskList.addAll(tasks);
        taskAdapter.notifyDataSetChanged();
    }
}
