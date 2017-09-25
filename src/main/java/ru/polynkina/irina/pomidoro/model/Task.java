package ru.polynkina.irina.pomidoro.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {

    private int id;
    private String description;
    private Priority priority;
    private GenerationType type;
    private LocalDate startDay;
    private LocalDate endDay;
    private LocalTime workTime;

    public Task(int id, String description, Priority priority, GenerationType type,
                        LocalDate startDay, LocalDate endDay, LocalTime timeWork) {
        this.id = id;
        this.description = description.trim();
        this.priority = priority;
        this.type = type;
        this.startDay = startDay;
        this.endDay = endDay;
        this.workTime = timeWork;
    }

    public Task(int id, String description, Priority priority, GenerationType type, LocalDate startDay, LocalDate endDay) {
        this(id, description, priority, type, startDay, endDay, LocalTime.of(0, 0, 0));
    }

    public Task(int id, String description, Priority priority, GenerationType type, LocalDate endDay) {
        this(id, description, priority, type, LocalDate.now(), endDay, LocalTime.of(0, 0, 0));
    }

    public Task(String description, Priority priority, GenerationType type, LocalDate endDay) {
        this(0, description, priority, type, endDay);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public GenerationType getType() {
        return type;
    }

    public void setType(GenerationType type) {
        this.type = type;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public LocalTime getWorkTime() {
        return workTime;
    }

    public void addWorkTime(LocalTime time) {
        workTime = workTime.plusHours(time.getHour());
        workTime = workTime.plusMinutes(time.getMinute());
        workTime = workTime.plusSeconds(time.getSecond());
    }

    public String getTextForSQL() {
        final String sign = "'";
        final String delimiter = "','";

        String SQLText = sign;
        SQLText += description + delimiter;
        SQLText += priority + delimiter;
        SQLText += type + delimiter;
        SQLText += startDay + delimiter;
        SQLText += endDay + delimiter;
        SQLText += workTime + sign;
        return SQLText;
    }

    public String[] getInfo() {
        return new String[] {
                description,
                priority.name(),
                endDay.toString(),
                workTime.toString()
        };
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", type=" + type +
                ", startDay=" + startDay +
                ", endDay=" + endDay +
                ", timeWork=" + workTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if(id != task.id) return false;
        if(description != null ? !description.equals(task.description) : task.description != null) return false;
        if(priority != task.priority) return false;
        if(type != task.type) return false;
        if(startDay != null ? !startDay.equals(task.startDay) : task.startDay != null) return false;
        if(endDay != null ? !endDay.equals(task.endDay) : task.endDay != null) return false;
        return workTime != null ? workTime.equals(task.workTime) : task.workTime == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (startDay != null ? startDay.hashCode() : 0);
        result = 31 * result + (endDay != null ? endDay.hashCode() : 0);
        result = 31 * result + (workTime != null ? workTime.hashCode() : 0);
        return result;
    }
}