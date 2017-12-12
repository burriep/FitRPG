package edu.uwm.cs.fitrpg.util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.activity.FitnessOverview;
import edu.uwm.cs.fitrpg.model.User;

public class NotificationHandler extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_REMINDER_ID = "Reminders";
    public static final String NOTIFICATION_CHANNEL_SERVICE_ID = "Ongoing Fitness";
    private static final String ACTION_FITNESS_REMINDER = "edu.uwm.cs.fitrpg.util.NotificationHandler.FITNESS_REMINDER";
    private static final String NOTIFICATION_TAG = "FitnessReminder";

    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel reminderChannel = nm.getNotificationChannel(NOTIFICATION_CHANNEL_REMINDER_ID);
        if (reminderChannel == null) {
            reminderChannel = new NotificationChannel(NOTIFICATION_CHANNEL_REMINDER_ID, NOTIFICATION_CHANNEL_REMINDER_ID, NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(reminderChannel);
        }
        NotificationChannel serviceChannel = nm.getNotificationChannel(NOTIFICATION_CHANNEL_SERVICE_ID);
        if (serviceChannel == null) {
            serviceChannel = new NotificationChannel(NOTIFICATION_CHANNEL_SERVICE_ID, NOTIFICATION_CHANNEL_SERVICE_ID, NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(serviceChannel);
        }
    }

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     */
    public static void notifyFitnessReminder(final Context context) {
        final Resources res = context.getResources();

        final String title = res.getString(R.string.fitness_reminder_notification_title_template);
        final String text = res.getString(R.string.fitness_reminder_notification_placeholder_text_template);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_REMINDER_ID)
                // Set appropriate defaults for the notification light, sound, and vibration.
                .setDefaults(Notification.DEFAULT_ALL)
                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.ic_stat_fitness_reminder)
                .setContentTitle(title)
                .setContentText(text)
                // All fields below this line are optional.
                // Use a default priority (recognized on devices running Android 4.1 or later)
                .setPriority(Notification.PRIORITY_DEFAULT)
                // Set the pending intent to be initiated when the user touches the notification.
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, FitnessOverview.class), PendingIntent.FLAG_UPDATE_CURRENT))
                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        final Notification notification = builder.build();
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, 0, notification);
    }

    /**
     * Sets if this class is enabled as a BroadcastReceiver for boot broadcasts.
     *
     * @param context
     * @param enabled
     */
    public static void setEnabledState(Context context, boolean enabled) {
        ComponentName receiver = new ComponentName(context, NotificationHandler.class);
        PackageManager pm = context.getPackageManager();
        int state = enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        pm.setComponentEnabledSetting(receiver, state, PackageManager.DONT_KILL_APP);
    }

    public static void setReminder(Context context, Date time) {
        Calendar timeOnlyCalendar = Calendar.getInstance();
        timeOnlyCalendar.setTime(time);
        Calendar calendar = Calendar.getInstance();
        // TODO: add a day
        calendar.set(Calendar.HOUR_OF_DAY, timeOnlyCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeOnlyCalendar.get(Calendar.MINUTE));
        setReminder(context, calendar);
    }

    public static void setReminder(Context context, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_FITNESS_REMINDER, null, context, NotificationHandler.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // TODO: change interval to AlarmManager.INTERVAL_DAY
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public static void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_FITNESS_REMINDER, null, context, NotificationHandler.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // If the alarm has been set, cancel it.
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("android.intent.action.BOOT_COMPLETED")) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            User user = User.get(db, 1);
            db.close();
            setReminder(context, user.getFitnessReminderDate());
        } else if (action != null && action.equals(ACTION_FITNESS_REMINDER)) {
            notifyFitnessReminder(context);
        }
    }
}
