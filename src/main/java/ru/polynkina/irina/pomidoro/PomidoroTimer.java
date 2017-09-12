package ru.polynkina.irina.pomidoro;

import java.util.concurrent.TimeUnit;

public class PomidoroTimer {

    private static final int TASK_TIME_IN_SECONDS = 5; //25 * 60;
    private static final int SHORT_PAUSE_IN_SECONDS = 1; //5 * 60;
    private static final int BIG_PAUSE_IN_SECONDS = 5; //15 * 60;

    private int timeWorkInSeconds;
    private int timePauseInSeconds;
    private int timeCurrentPause;

    private boolean isTimeWork;
    private boolean isTimePause;

    private int amountCyclesWork;
    private int amountCyclesPause;
    private int amountFullCycles;

    public PomidoroTimer() {
        isTimeWork = true;
    }

    public boolean isTimeWork() {
        return isTimeWork;
    }

    public boolean isTimePause() {
        return isTimePause;
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
                }
            } else {
                if(++timePauseInSeconds == timeCurrentPause) {
                    timePauseInSeconds = 0;
                    isTimeWork = true;
                    isTimePause = false;
                    ++amountCyclesPause;
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
}
