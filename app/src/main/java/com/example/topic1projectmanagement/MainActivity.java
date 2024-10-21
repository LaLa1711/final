package com.example.topic1projectmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    RecyclerView recyclerView;
    TaskAdapter adapter;
    EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Task> taskList = dbHelper.getAllTasks();
        adapter = new TaskAdapter(taskList, dbHelper, this);
        recyclerView.setAdapter(adapter);

        ImageButton addButton = findViewById(R.id.addTaskButton);
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        ImageButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        ImageButton ganttChartButton = findViewById(R.id.ganttChartButton);
        ganttChartButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GanttChartActivity.class);
            startActivity(intent);
        });

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(view -> {
            adapter.removeSelectedTasks();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Task> taskList = dbHelper.getAllTasks();
        adapter.updateTaskList(taskList);
    }
}
