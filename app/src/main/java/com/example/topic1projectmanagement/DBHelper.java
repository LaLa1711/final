
package com.example.topic1projectmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, taskName TEXT, assignee TEXT, startDate TEXT, endDate TEXT, estimateDays INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }


    public void addTask(Task task) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskName", task.getName());
        values.put("assignee", task.getAssigneeName());
        values.put("startDate", task.getStartDate());
        values.put("endDate", task.getEndDate());
        values.put("estimateDays", task.getEstimateDays());
        db.insert("tasks", null, values);
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks ORDER BY id", null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5)
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public Task getTaskById(int taskId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("tasks", null, "id = ?", new String[]{String.valueOf(taskId)}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("taskName");
                int assigneeIndex = cursor.getColumnIndex("assignee");
                int startDateIndex = cursor.getColumnIndex("startDate");
                int endDateIndex = cursor.getColumnIndex("endDate");

                int estimateDaysIndex = cursor.getColumnIndex("estimateDays");


                if (idIndex != -1 && nameIndex != -1 && startDateIndex != -1 &&
                        endDateIndex != -1 && assigneeIndex != -1 && estimateDaysIndex != -1) {

                    Task task = new Task(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(assigneeIndex),
                            cursor.getString(startDateIndex),
                            cursor.getString(endDateIndex),

                            cursor.getInt(estimateDaysIndex)
                    );
                    cursor.close();
                    return task;
                }
            }
            cursor.close();
        }
        return null;
    }


    public void updateTask(Task task) throws ParseException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskName", task.getName());
        values.put("assignee", task.getAssigneeName());
        values.put("startDate", task.getStartDate());
        values.put("endDate", task.getEndDate());
        values.put("estimateDays", task.getEstimateDays());
        db.update("tasks", values, "id = ?", new String[]{String.valueOf(task.getId())});
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "id = ?", new String[]{String.valueOf(taskId)});
        db.close();
    }
    public List<Task> getTasksWithinDateRange(Date startDate, Date endDate) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startDateStr = dbFormat.format(startDate);
        String endDateStr = dbFormat.format(endDate);

        String query = "SELECT * FROM tasks WHERE " +
                "strftime('%Y-%m-%d', substr(startDate, 7, 4) || '-' || substr(startDate, 4, 2) || '-' || substr(startDate, 1, 2)) >= ? " +
                "AND strftime('%Y-%m-%d', substr(endDate, 7, 4) || '-' || substr(endDate, 4, 2) || '-' || substr(endDate, 1, 2)) <= ?";

        Cursor cursor = db.rawQuery(query, new String[]{startDateStr, endDateStr});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndexOrThrow("id");
                    int nameIndex = cursor.getColumnIndexOrThrow("taskName");
                    int assigneeIndex = cursor.getColumnIndexOrThrow("assignee");
                    int startDateIndex = cursor.getColumnIndexOrThrow("startDate");
                    int endDateIndex = cursor.getColumnIndexOrThrow("endDate");
                    int estimateDaysIndex = cursor.getColumnIndexOrThrow("estimateDays");

                    Task task = new Task(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(assigneeIndex),
                            cursor.getString(startDateIndex),
                            cursor.getString(endDateIndex),
                            cursor.getInt(estimateDaysIndex)
                    );
                    tasks.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return tasks;
    }




    public List<Task> searchTasksByIdOrName(String query) {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM tasks WHERE taskName LIKE ? OR assignee LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"}
        );

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5)
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
}