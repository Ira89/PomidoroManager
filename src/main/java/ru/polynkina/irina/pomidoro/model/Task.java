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
    private int amountDaysWork;

    public Task(int id, String description, Priority priority, GenerationType type,
                        LocalDate startDay, LocalDate endDay, LocalTime timeWork, int amountDaysWork) {
        this.id = id;
        this.description = description.trim();
        this.priority = priority;
        this.type = type;
        this.startDay = startDay;
        this.endDay = endDay;
        this.workTime = timeWork;
        this.amountDaysWork = amountDaysWork;
    }

    public Task(int id, String description, Priority priority, GenerationType type,
                LocalDate startDay, LocalDate endDay, LocalTime timeWork) {
        this(id, description, priority, type, startDay, endDay, timeWork, 0);
    }

    public Task(int id, String description, Priority priority, GenerationType type, LocalDate startDay, LocalDate endDay) {
        this(id, description, priority, type, startDay, endDay, LocalTime.of(0, 0, 0));
    }

    public Task(int id, String description, Priority priority, GenerationType type, LocalDate endDay) {
        this(id, description, priority, type, LocalDate.now(), endDay);
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

    public int getAmountDaysWork() {
        return amountDaysWork;
    }

    public void addWorkTime(LocalTime time) {
        LocalTime currTime = LocalTime.of(workTime.getHour(), workTime.getMinute(), workTime.getSecond());
        workTime = workTime.plusHours(time.getHour());
        workTime = workTime.plusMinutes(time.getMinute());
        workTime = workTime.plusSeconds(time.getSecond());
        if(currTime.isAfter(workTime)) ++amountDaysWork;
    }

    public String getTextForSQL() {
        final String delimiter = "','";

        String SQLText = "'";
        SQLText += description + delimiter;
        SQLText += priority + delimiter;
        SQLText += type + delimiter;
        SQLText += startDay + delimiter;
        SQLText += endDay + delimiter;
        SQLText += workTime + "',";
        SQLText += amountDaysWork;
        return SQLText;
    }

    public String[] getInfo() {
        String time;
        if(amountDaysWork == 0) time = workTime.toString();
        else time = amountDaysWork + " дн " + workTime.toString();
        return new String[] {
                description,
                priority.name(),
                endDay.toString(),
                time
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
                ", amountDaysWork=" + amountDaysWork +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if(id != task.id) return false;
        if(amountDaysWork != task.amountDaysWork) return false;
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
        result = 31 * result + amountDaysWork;
        result = 31 * result + (workTime != null ? workTime.hashCode() : 0);
        return result;
    }
}