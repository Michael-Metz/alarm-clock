import javax.swing.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class that represents an alarm.
 */
public class Alarm {

    private static Timer timer = new Timer();
    private Date date;
    private String message;
    private int snoozeCount;

    public Alarm(Date date, String message) {
        this(date, message, 0);
    }

    public Alarm(Date date, String message, int snoozeCount) {
        this.date = date;
        this.message = message;
        this.snoozeCount = snoozeCount;
        setAlarm();
    }

    public void setAlarm() {
        //Now create the time and schedule it
        Timer timer = new Timer();

        //Use this if you want to execute it once
        AlarmTimerTask t = new AlarmTimerTask();
        timer.schedule(t, date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public int getSnoozeCount() {
        return snoozeCount;
    }
    public Alarm getInstance(){
        return this;
    }

    /**
     * private inner class of alarm that handles sechudling timers to trigger
     */
    private class AlarmTimerTask extends TimerTask {
        private Object[] options = {"Snooze", "Dismiss"};

        /**
         * This method is called when the alarm is triggered.
         */
        public void run() {
            if (snoozeCount != 0)
                options[0] = "Snooze(" + snoozeCount + ")";

            int choice = -1;
            choice = JOptionPane.showOptionDialog(null, "Alarm", "alaram", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);

            //handle choice
            if (choice == 0) {
                //snooze current alarm and set it for 1 minute from now
                snoozeCount++;
                Date snooze = new Date();
                snooze.setTime(60000 + snooze.getTime());
                setDate(snooze);
                setAlarm();
                Model.getInstance().save();
            } else if (choice == 1) {
                //dismiss
                //todo remove time  the alarm from the array
                Model db = Model.getInstance();
                Alarm thisAlarm = getInstance();
                db.removeAlarm(thisAlarm);
            }
        }
    }
}
