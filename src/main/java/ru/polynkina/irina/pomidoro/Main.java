package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.gui.PomidoroFrame;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(new Runnable() {
            public void run() {
                JFrame frame = new PomidoroFrame();
            }
        });
    }
}
