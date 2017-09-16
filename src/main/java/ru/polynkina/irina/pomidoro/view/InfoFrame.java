package ru.polynkina.irina.pomidoro.view;

import javax.swing.*;
import java.awt.*;

public class InfoFrame extends JDialog {

    public InfoFrame(JFrame owner, String text) {
        super(owner, text, true);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(size.width / 4, size.height / 4);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
