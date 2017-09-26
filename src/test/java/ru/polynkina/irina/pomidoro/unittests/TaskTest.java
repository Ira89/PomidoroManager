package ru.polynkina.irina.pomidoro.unittests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.polynkina.irina.pomidoro.model.GenerationType;
import ru.polynkina.irina.pomidoro.model.Priority;
import ru.polynkina.irina.pomidoro.model.Task;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskTest {

    private static Task task;

    @BeforeClass
    public static void init() {
        task = new Task("Test", Priority.A, GenerationType.ONCE, LocalDate.now());
    }

    @Test
    public void testAddTime() {
        LocalTime time = LocalTime.of(15, 12, 35);
        task.addWorkTime(time);
        Assert.assertTrue(task.getWorkTime().equals(time));

        task.addWorkTime(LocalTime.of(5, 50, 35));
        Assert.assertTrue(task.getWorkTime().equals(LocalTime.of(21, 3, 10)));

        task.addWorkTime(LocalTime.of(3, 0, 0));
        Assert.assertTrue(task.getWorkTime().equals(LocalTime.of(0, 3, 10)));
    }
}
