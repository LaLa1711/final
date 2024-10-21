package com.example.topic1projectmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private RecyclerView resultsRecyclerView;
    private TaskAdapter taskAdapter;
    private ListView resultsListView;
    private DBHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Task> resultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DBHelper(this);

        searchEditText = findViewById(R.id.searchEditText);
        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);

        resultsList = new ArrayList<>();
        taskAdapter = new TaskAdapter(resultsList , dbHelper, this);
        resultsRecyclerView.setAdapter(taskAdapter);
        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    try {
                        searchTasks(query); // Gọi hàm tìm kiếm
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Please enter a task name or ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchTasks(String query) throws ParseException {
        resultsList.clear();

        List<Task> foundTasks = dbHelper.searchTasksByIdOrName(query);
        for (Task task : foundTasks) {
            resultsList.add(task);
        }
        taskAdapter.notifyDataSetChanged();

        if (resultsList.isEmpty()) {
            Toast.makeText(this, "No tasks found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
