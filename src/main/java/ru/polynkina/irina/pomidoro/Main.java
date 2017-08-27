package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.gui.PomidoroFrame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

       List<Task> tasks = new ArrayList<Task>();
       tasks.add(new Task("сделать x", Priority.A, GenerationType.EVERY_DAY, LocalDate.of(2017, 9, 30)));
       tasks.add(new Task("сделать y", Priority.B, GenerationType.EVERY_MONTH, LocalDate.of(2017, 9, 15)));
       tasks.add(new Task("сделать z", Priority.C, GenerationType.EVERY_WEEK, LocalDate.of(2017, 10, 30)));
       tasks.add(new Task("сделать abc", Priority.D, GenerationType.ONCE, LocalDate.of(2017, 10, 15)));
       tasks.add(new Task("12345678 очень-очень-очень длинное описание задачи", Priority.D, GenerationType.ONCE, LocalDate.of(2017, 10, 15)));

        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                JFrame frame = new PomidoroFrame(tasks);
            }
        });
    }
}
