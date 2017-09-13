package ru.polynkina.irina.pomidoro;

import ru.polynkina.irina.pomidoro.view.OkFrame;

import java.util.concurrent.TimeUnit;

public class PomidoroTimer {

    private static final int TASK_TIME_IN_SECONDS = 25 * 60;
    private static final int SHORT_PAUSE_IN_SECONDS = 5 * 60;
    private static final int BIG_PAUSE_IN_SECONDS = 15 * 60;

    private int timeWorkInSeconds;
    private int timePauseInSeconds;
    private int timeCurrentPause;

    private boolean isTimeWork;
    private boolean isTimePause;

    private int amountCyclesWork;
    private int amountCyclesPause;

    public PomidoroTimer() {
        isTimeWork = true;
    }

    public boolean isTimeWork() {
        return isTimeWork;
    }

    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
            timeCurrentPause = (amountCyclesWork + amountCyclesPause) % 7 == 0 ? BIG_PAUSE_IN_SECONDS : SHORT_PAUSE_IN_SECONDS;
            if(isTimeWork) {
                if(++timeWorkInSeconds == TASK_TIME_IN_SECONDS) {
                    timeWorkInSeconds = 0;
                    isTimeWork = false;
                    isTimePause = true;
                    ++amountCyclesWork;
                    OkFrame frame = new OkFrame(null, "Перерыв!");
                    while(frame.isVisible()){}
                }
            } else {
                if(++timePauseInSeconds == timeCurrentPause) {
                    timePauseInSeconds = 0;
                    isTimeWork = true;
                    isTimePause = false;
                    ++amountCyclesPause;
                    OkFrame frame = new OkFrame(null,"За работу!");
                    while(frame.isVisible()){}
                }
            }
        } catch(InterruptedException exc) {
            exc.printStackTrace();
            System.exit(-1);
        }
    }

    public int getTimePauseUntilEnd() {
        return timeCurrentPause - timePauseInSeconds;
    }

    public int getTimeWorkUntilEnd() {
        return TASK_TIME_IN_SECONDS - timeWorkInSeconds;
    }

    public int getAllTimeWork() {
        return timeWorkInSeconds + amountCyclesWork * TASK_TIME_IN_SECONDS;
    }

    public void reset() {
        isTimeWork = true;
        isTimePause = false;
        timeWorkInSeconds = 0;
        timePauseInSeconds = 0;
        timeCurrentPause = 0;
        amountCyclesWork = 0;
        amountCyclesPause = 0;
    }
}
