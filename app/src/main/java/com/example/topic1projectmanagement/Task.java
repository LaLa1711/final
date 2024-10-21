package com.example.topic1projectmanagement;

public class Task {
    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private String assigneeName;
    private int estimateDays;
    private boolean isChecked;

    public Task(int id, String name, String assigneeName, String startDate, String endDate, int estimateDays) {
        this.id = id;
        this.name = name;
        this.assigneeName = assigneeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.estimateDays = estimateDays;
        this.isChecked = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getEstimateDays() {

        return estimateDays;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
