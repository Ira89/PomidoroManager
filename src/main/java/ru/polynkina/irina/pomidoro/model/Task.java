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
        this.description = description;
        this.priority = priority;
        this.type = type;
        this.startDay = startDay;
        this.endDay = endDay;
        this.timeWork = timeWork;
    }

    public Task(int id, String description, Priority priority, GenerationType type, LocalDate endDay) {
        this(id, description, priority, type, LocalDate.now(), endDay, LocalTime.of(0, 0, 0));
    }

    public Task(String description, Priority priority, GenerationType type, LocalDate endDay) {
        this(0, description, priority, type, endDay);
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

    public void printInfo() {
        System.out.print(id + " ");
        System.out.print(description.trim() + " ");
        System.out.print(priority + " ");
        System.out.print(type + " ");
        System.out.print(startDay + " ");
        System.out.print(endDay + " ");
        System.out.println(timeWork + " ");
    }
}
