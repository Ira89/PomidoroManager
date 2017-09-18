package ru.polynkina.irina.pomidoro.view.okCancelDialogs;

import ru.polynkina.irina.pomidoro.model.Task;

import javax.swing.*;
import java.awt.*;

public abstract class OkCancelDialog extends JDialog {

    private JPanel textPanel;
    private JLabel text;

    private JPanel buttonPanel;
    private JButton ok;
    private JButton cancel;

    private boolean actionsIsSuccessful;


    public OkCancelDialog(JFrame owner, String name, Task task) {
        super(owner, name, true);
        setLocation(owner.getX(), owner.getY());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        createTextPanel(task, setActionDescription());
        createButtonPanel();
        createView();
    }

    public abstract String setActionDescription();

    public abstract void okAction();

    public abstract void cancelAction();

    private void createTextPanel(Task task, String actionDescription) {
        textPanel = new JPanel();
        text = new JLabel();
        text.setText("<html><br>" +
                "Вы действительно хотите " + actionDescription +
                "<br>" + task.getDescription() + "?<br>" +
                "<br></html>");
        text.setVerticalAlignment(JLabel.CENTER);
        text.setForeground(Color.BLUE);
        textPanel.add(text);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        ok = new JButton("Ок");
        ok.addActionListener(e -> {
            actionsIsSuccessful = true;
            okAction();
            dispose();
        });
        buttonPanel.add(ok);

        cancel = new JButton("Отмена");
        cancel.addActionListener(e -> {
            actionsIsSuccessful = false;
            cancelAction();
            dispose();
        });
        buttonPanel.add(cancel);
    }

    private void createView() {
        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    public boolean userActionsIsSuccessful() {
        return actionsIsSuccessful;
    }
}
