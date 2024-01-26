package com.example.myapplication;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class to assign the timer properties to the card views int the recycler view
 */
public class Model {
    private String timer_name;
    public long time_left;

    public CountdownTask count_down_task;
    public Integer TOTAL_TIME;
    private String progress_bar;
    private Integer progress;
    private boolean timer_running;
    private Context context;
    String CHANNEL_ID = "timer_notifications";
    RecyclingAdaptor cardAdapter;
    //CountdownTask task;

    /**
     * Constructor method
     * @param timer_name name of the timer
     * @param total_time the length of the timer
     * @param context the activity that the model will be in
     * @param cardAdapter the adapter that will display the timer
     */
    public Model(String timer_name, int total_time, Context context, RecyclingAdaptor cardAdapter) {
        this.timer_name = timer_name;
        this.TOTAL_TIME = total_time * (1000 * 60);
        this.time_left = total_time * (1000 * 60);
        this.progress = 0;
        this.context = context;
        this.cardAdapter = cardAdapter;
        timer_running = false;
    }

    /**
     * sends notifications to the users phone
     */
    public void sendNotification() {
        //creates the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_panorama_photosphere_24)
                .setContentTitle(timer_name + " Finished!")
                .setContentText("Your timer has finished.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //sends the notification via a notification manager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (notificationManager.areNotificationsEnabled() && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        } else {
            // Show a message to the user indicating that notifications are disabled
            Toast.makeText(context, "Notifications are disabled", Toast.LENGTH_SHORT).show();
        }
        // send a notification
    }

    public void setTimeLeft(int time_left) {
        this.time_left = time_left;
    }

    /**
     * Getter for the timer name
     * @return String timer name
     */
    public String getTimerName() {
        return timer_name;
    }

    /**
     * Sets the timer name
     */
    public void setTimerName(String timer_name) {
        this.timer_name = timer_name;
    }

    /**
     * Gets the time left of a timer in HR:MIN form
     * @return String time_left_form
     */
    public String getTimeLeft() {
        if (time_left <= 0) {
            timer_running = false;
            return "00:00";
        }
        int minutes = (int) (time_left / 1000) / 60 / 60;
        int seconds = (int) (time_left / 1000) / 60 % 60;

        String time_left_form = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        return time_left_form;
    }

    /**
     * Used for getting the progress of the timer
     * @return Integer progress
     */
    public long getProgress() {
        double progress_long = 100 - ((double) time_left / TOTAL_TIME)*100;
        int progress_return = (int) progress_long;
        return progress_return;
    }

    /**
     * Gets the state of the timer
     * @return boolean timer_running
     */
    public boolean getTimerState() {
        return timer_running;
    }

    /**
     * Run when the timer finishes and sends notifications to the phone
     */
    private void onFinish() {
        //creates the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_panorama_photosphere_24)
                .setContentTitle(timer_name + " Finished!")
                .setContentText("Your timer has finished.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //sends the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (notificationManager.areNotificationsEnabled() && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        } else {
            // Show a message to the user indicating that notifications are disabled
            Toast.makeText(context, "Notifications are disabled", Toast.LENGTH_SHORT).show();
        }
        // send a notification


    }

    /**
     * Starts the visual timer
     */
    public void startTimer() {
        count_down_task = new CountdownTask(this);
        timer_running = true;
        count_down_task.execute();
    }

    /**
     * Pauses the timer
     */
    public void pauseTimer() {
        timer_running = false;
    }

    /**
     * Resets the timer
     */
    public void resetTimer() {
        time_left = TOTAL_TIME;
        timer_running = false;
        cardAdapter.notifyDataSetChanged();
    }

    /**
     * A AsyncTask that counts down the time left on a timer while the user is on the page
     */
    public class CountdownTask extends AsyncTask<Model, Void, Void> {

        private Model model;

        /**
         * Constructor
         * @param model the outer class instance
         */
        public CountdownTask(Model model) {
            this.model = model;
        }

        /**
         * The timer (run on a diffrent thread so it does not effect the UI speed)
         * @param params passes in the model
         * @return null
         */
        @Override
        protected Void doInBackground(Model... params) {
            try {
                sleep(5000);
                model.time_left = model.time_left - 5000;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        /**
         * Runs when the task is finished to update th UI
         * @param empty Void null
         */
        @Override
        protected void onPostExecute(Void empty) {
            //Updates the UI
            model.cardAdapter.notifyDataSetChanged();
            //Stops the timer if it has run out of time
            if(model.time_left <= 0) {
                onFinish();
                timer_running = false;
                model.progress = 100;
            }
            //If the timer has not stopped will restart the task
            else if (model.timer_running) {
                try {
                    double progress_long = 100 - ((double) model.time_left / model.TOTAL_TIME)*100;
                    progress = (int) progress_long;
                }catch (Exception e) {
                    progress = 0;
                }
                CountdownTask next_count = new CountdownTask(model);
                model.count_down_task = next_count;
                count_down_task.execute();
            }
        }
    }
}
