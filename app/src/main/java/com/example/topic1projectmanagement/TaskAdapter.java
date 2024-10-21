package com.example.topic1projectmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> tasks;
    private DBHelper dbHelper;
    private Context context;

    public TaskAdapter(List<Task> tasks, DBHelper dbHelper, Context context) {
        this.tasks = tasks;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskName.setText(task.getName());
        holder.taskStartDate.setText(task.getStartDate());
        holder.taskEndDate.setText(task.getEndDate());
        holder.taskAssignee.setText(task.getAssigneeName());
        holder.checkBox.setChecked(task.isChecked());

        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean isEstimateDayVisible = sharedPreferences.getBoolean("EstimateDayVisible", true);

        if (isEstimateDayVisible) {
            holder.taskEstimateDays.setText(task.getEstimateDays() + " days");
            holder.taskEstimateDays.setVisibility(View.VISIBLE);
        } else {
            holder.taskEstimateDays.setVisibility(View.GONE);
        }

        holder.editButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("taskId", task.getId());
            context.startActivity(intent);
        });

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTaskList(List<Task> newTasks) {
        this.tasks.clear();
        this.tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public void removeSelectedTasks() {
        List<Task> tasksToRemove = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isChecked()) {
                tasksToRemove.add(task);
            }
        }

        for (Task task : tasksToRemove) {
            dbHelper.deleteTask(task.getId());
            tasks.remove(task);
        }

        notifyDataSetChanged();
        Toast.makeText(context, tasksToRemove.size() + " tasks deleted.", Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskStartDate;
        TextView taskEndDate;
        TextView taskAssignee;
        TextView taskEstimateDays;
        Button editButton;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.taskName);
            taskStartDate = itemView.findViewById(R.id.taskStartDate);
            taskEndDate = itemView.findViewById(R.id.taskEndDate);
            taskAssignee = itemView.findViewById(R.id.taskAssignee);
            taskEstimateDays = itemView.findViewById(R.id.taskEstimateDays);
            editButton = itemView.findViewById(R.id.editButton);
            checkBox = itemView.findViewById(R.id.checkBox); 
        }
    }
}
