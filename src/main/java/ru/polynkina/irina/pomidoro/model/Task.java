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
    private LocalTime timeWork;

    public Task(int id, String description, Priority priority, GenerationType type,
                        LocalDate startDay, LocalDate endDay, LocalTime timeWork) {
        this.id = id;
        this.description = description.trim();
        this.priority = priority;
        this.type = type;
        this.startDay = startDay;
        this.endDay = endDay;
        this.timeWork = timeWork;
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

    public Priority getPriority() {
        return priority;
    }

    public GenerationType getType() {
        return type;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
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
        SQLText += timeWork + sign;
        return SQLText;
    }

    public String[] getInfo() {
        return new String[] {
                description,
                priority.name(),
                endDay.toString(),
                timeWork.toString()
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
                ", timeWork=" + timeWork +
                '}';
    }
}
