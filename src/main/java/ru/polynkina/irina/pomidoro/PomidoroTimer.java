package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.view.InfoFrame;

import javax.swing.*;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class PomidoroTimer implements Runnable {

    private static final int TASK_TIME_IN_SECONDS = 25 * 60;
    private static final int SHORT_PAUSE_IN_SECONDS = 5 * 60;
    private static final int BIG_PAUSE_IN_SECONDS = 15 * 60;

    private int timeWorkInSeconds;
    private int timePauseInSeconds;
    private int timeCurrentPause;

    private boolean isTimeWork;

    private int amountCyclesWork;

    private InfoFrame pause;
    private InfoFrame work;

    private JTextField textField;

    public PomidoroTimer(JTextField textField) {
        isTimeWork = true;
        pause = new InfoFrame(null, "Перерыв!", "Время сделать перерыв");
        pause.setAlwaysOnTop(true);
        work = new InfoFrame(null, "За работу!", "Пора приступать к работе");
        work.setAlwaysOnTop(true);
        this.textField = textField;
    }

    public void reset() {
        isTimeWork = true;
        timeWorkInSeconds = 0;
        timePauseInSeconds = 0;
        timeCurrentPause = 0;
        amountCyclesWork = 0;
    }

    public int getAllTimeWork() {
        return timeWorkInSeconds + amountCyclesWork * TASK_TIME_IN_SECONDS;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                TimeUnit.SECONDS.sleep(1);
                if(amountCyclesWork != 0 && amountCyclesWork % 4 == 0) timeCurrentPause = BIG_PAUSE_IN_SECONDS;
                else timeCurrentPause = SHORT_PAUSE_IN_SECONDS;
                if(isTimeWork) {
                    if(timeWorkInSeconds++ == TASK_TIME_IN_SECONDS) {
                        timeWorkInSeconds = 0;
                        isTimeWork = false;
                        ++amountCyclesWork;
                        pause.setVisible(true);
                    }
                } else {
                    if(timePauseInSeconds++ == timeCurrentPause) {
                        timePauseInSeconds = 0;
                        isTimeWork = true;
                        work.setVisible(true);
                    }
                }
            } catch(InterruptedException exc) {
                Thread.currentThread().interrupt();
                textField.setText("");
            }
            if(textField != null) writeInTextField();
        }
    }

    private int getTimePauseUntilEnd() {
        int currentPause = (amountCyclesWork != 0 && amountCyclesWork % 4 == 0) ? BIG_PAUSE_IN_SECONDS : SHORT_PAUSE_IN_SECONDS;
        return currentPause - timePauseInSeconds;
    }

    private int getTimeWorkUntilEnd() {
        return TASK_TIME_IN_SECONDS - timeWorkInSeconds;
    }

    private void writeInTextField() {
        if(isTimeWork) textField.setText("Время работы: " + LocalTime.ofSecondOfDay(getTimeWorkUntilEnd()).toString());
        else textField.setText("Время отдыха: " + LocalTime.ofSecondOfDay(getTimePauseUntilEnd()).toString());
    }
}