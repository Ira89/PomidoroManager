package ru.polynkina.irina.pomidoro;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {

    private static long counter = 0;

    private final long id;
    private String description;
    private Priority priority;
    private GenerationType type;
    private LocalDate startDay;
    private LocalDate endDay;
    private LocalTime timeWork;

    public Task(String description, Priority priority, GenerationType type, LocalDate endDay) {
        id = counter++;
        this.description = description;
        this.priority = priority;
        this.type = type;
        startDay = LocalDate.now();
        this.endDay = endDay;
        timeWork = LocalTime.of(0, 0, 0);
    }

    public String[] getInfo() {
        return new String[] {
                description,
                priority.name(),
                endDay.toString(),
                timeWork.toString()
        };
    }
}
